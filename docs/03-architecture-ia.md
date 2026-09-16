# Guidly — Architecture IA

Ce document détaille comment le service IA transforme une phrase écrite en langage libre en un parcours administratif structuré, sans jamais inventer d'information. C'est la pièce la plus sensible du projet, donc je prends le temps de justifier chaque choix.

## Vue d'ensemble du pipeline

```
Message utilisateur (FR / EN / AR / darija)
        │
        ▼
┌───────────────────┐
│ 1. Language        │  détecte la langue du message
│    Detection       │
└─────────┬──────────┘
          ▼
┌───────────────────┐
│ 2. Intent          │  associe le message à un intent_code
│    Detection       │  (ou aucun, si hors périmètre)
└─────────┬──────────┘
          ▼
┌───────────────────┐
│ 3. Entity          │  extrait âge, ville, statut,
│    Extraction      │  première demande / renouvellement...
└─────────┬──────────┘
          ▼
┌───────────────────┐
│ 4. Confidence       │  < 0.60 → clarification
│    Gating           │  0.60–0.85 → 2-3 propositions
│                     │  ≥ 0.85 → procédure directe
└─────────┬──────────┘
          ▼
┌───────────────────┐
│ 5. Procedure        │  requête SQL sur intent_code
│    Retrieval        │  (jamais de recherche floue sur le titre)
└─────────┬──────────┘
          ▼
┌───────────────────┐
│ 6. Rule             │  filtre étapes/documents selon le
│    Evaluation        │  profil (target_users, condition...)
└─────────┬──────────┘
          ▼
┌───────────────────┐
│ 7. Explanation       │  le LLM reformule UNIQUEMENT les
│    (LLM)             │  données déjà récupérées en base
└─────────┬──────────┘
          ▼
   Réponse structurée + sources
```

Le point que je veux garder visible à chaque étape : **le LLM n'intervient qu'aux étapes 2 (classification), 3 (extraction) et 7 (reformulation)**. Il n'a jamais la main pour décider seul du contenu d'une procédure — ça reste la base de données, via `intent_code`.

---

## 1. Language Detection

Avant même de chercher l'intention, je détermine la langue du message (`fr`, `en`, `ar`, `dar`). C'est nécessaire pour deux raisons :

- adapter le prompt envoyé au LLM (few-shot examples dans la bonne langue) ;
- alimenter `ai_query_logs.detected_language`, pour pouvoir mesurer la précision séparément par langue — ce qui est important puisque le français/anglais et la darija n'ont pas du tout le même niveau de fiabilité avec les outils actuels.

Pour le français, l'anglais et l'arabe standard, un détecteur de langue classique (type `fasttext` ou équivalent léger) suffit largement. Pour la darija — qui s'écrit souvent en alphabet latin ("bghit ndir passport") — la détection est plus ambiguë, donc en pratique je traite le cas "latin script + mots-clés darija connus" comme une heuristique complémentaire plutôt que de compter uniquement sur un détecteur de langue générique.

## 2. Intent Detection

C'est l'étape centrale : associer le message à un `intent_code` existant dans la table `procedures`, ou décider qu'aucun intent ne correspond.

**Approche retenue pour le MVP** : classification par le LLM, contraint à une liste fermée d'intents possibles (ceux présents en base), avec sortie structurée en JSON.

Exemple de prompt (simplifié) :

```
Tu es un classifieur d'intentions pour un assistant administratif.
Voici la liste des intents disponibles : [passport_renewal, passport_first_request,
university_registration, business_creation, ...]

Message utilisateur : "{message}"

Réponds uniquement en JSON :
{
  "intent": "<un des intents ci-dessus, ou null si aucun ne correspond>",
  "confidence": <valeur entre 0 et 1>,
  "reasoning": "<courte justification interne>"
}
```

Pourquoi cette approche plutôt qu'un modèle de classification entraîné sur mesure : avec seulement 5 à 10 procédures au MVP, entraîner un classifieur dédié n'a pas de sens — je n'ai pas assez de données, et ça ajouterait de la complexité pour un gain minime. Un LLM contraint à une liste fermée d'intents donne de bons résultats à cette échelle, et reste facile à faire évoluer quand j'ajouterai des procédures : il suffit d'étendre la liste, pas de ré-entraîner un modèle.

**Le point important** : je ne laisse jamais le LLM inventer un intent qui n'existe pas dans la liste transmise. La liste vient toujours dynamiquement de la base (`SELECT intent_code FROM procedures WHERE status = 'PUBLISHED'`), jamais codée en dur dans le prompt.

## 3. Entity Extraction

En parallèle (ou juste après) la détection d'intent, j'extrais les entités utiles à la personnalisation :

```json
{
  "age_category": "young_adult",
  "status": "student",
  "request_type": "first_request",
  "city": "Fès"
}
```

Même logique que pour l'intent : extraction contrainte à un schéma JSON fixe (pas de champs libres non prévus), pour que le résultat soit directement exploitable par le moteur de règles sans parsing fragile.

Je ne cherche jamais à extraire plus d'informations que nécessaire (pas de nom, pas de numéro de téléphone, etc.) — seulement ce qui sert réellement à personnaliser le parcours (section "Besoins non fonctionnels" : minimiser la donnée personnelle stockée).

## 4. Gestion de la confiance (Confidence Gating)

```
confidence >= 0.85   → la procédure est affichée directement
0.60 <= confidence < 0.85 → 2 à 3 procédures proposées, l'utilisateur choisit
confidence < 0.60    → demande de clarification, pas de procédure affichée
```

Ces seuils sont volontairement des points de départ — je compte les ajuster une fois que j'aurai testé le système sur mon dataset multilingue (section 32). Concrètement, je m'attends à devoir ajuster la darija séparément, puisque sa fiabilité est structurellement plus faible que le français/anglais.

**Exemple de clarification générée** (jamais de simple "je n'ai pas compris") :

> "Pour mieux vous aider, pouvez-vous préciser si vous souhaitez faire une première demande de passeport ou un renouvellement ?"

La formulation de la clarification est elle-même générée par le LLM, mais à partir des 2-3 intents candidats — jamais à partir de rien.

## 5. Procedure Retrieval

Une fois l'`intent_code` déterminé (ou choisi par l'utilisateur parmi les propositions), le backend fait une requête directe :

```sql
SELECT * FROM procedures WHERE intent_code = :intent_code AND status = 'PUBLISHED';
```

Puis récupère les étapes, documents et sources associés via les tables de liaison (`procedure_steps`, `procedure_documents`, `sources`). **Aucune recherche floue, aucun embedding à ce stade du MVP** — l'`intent_code` fait déjà le travail de matching exact. C'est un choix de simplicité assumé (section 15/40 du cadrage initial) : je garderai la recherche sémantique/RAG pour une itération future, si le nombre de procédures grandit au point qu'un simple mapping intent → procédure ne suffit plus.

## 6. Rule Evaluation

Le moteur de règles filtre ce qui est réellement pertinent pour le profil extrait à l'étape 3 :

- documents dont `procedure_documents.condition` correspond au profil (ex : "si première demande" → affiché seulement si `request_type = first_request`) ;
- étapes optionnelles (`required = false`) affichées différemment ;
- `target_users` de la procédure comparé au profil, pour ne montrer que ce qui s'applique réellement.

Cette étape reste volontairement simple pour le MVP : des règles conditionnelles basiques (correspondance de champs), pas un moteur de règles complexe type Drools — ça serait disproportionné vu le nombre de procédures visé.

## 7. Explanation (génération finale)

Dernière étape : le LLM reçoit **uniquement** les données déjà filtrées (procédure, étapes retenues, documents retenus, sources) et les reformule en langage simple. Le prompt est structuré pour rendre l'hallucination structurellement difficile :

```
Voici les informations vérifiées sur cette démarche (ne rien ajouter,
ne rien inventer, reformuler uniquement) :

{données JSON de la procédure filtrée}

Reformule ces informations en langage simple et bienveillant, dans la
langue de l'utilisateur ({langue détectée}). N'ajoute aucune information
qui n'est pas dans les données ci-dessus.
```

Le même principe s'applique à l'explication d'un terme administratif (section 9 du cadrage) : le LLM reformule le champ `glossary_terms.explanation` déjà stocké en base, il n'improvise jamais une définition.

## Comment j'évite les hallucinations, concrètement

Ce n'est pas une seule mesure mais plusieurs couches :

1. **Architecture retrieval-first** : le LLM ne voit jamais une question sans les données déjà récupérées en base — il n'a donc rien à "inventer", seulement à reformuler.
2. **Intents en liste fermée** : impossible pour le LLM de créer un intent qui n'existe pas en base.
3. **Prompts contraints** ("ne rien ajouter, reformuler uniquement") à chaque étape de génération de texte.
4. **Réponse explicite en cas d'absence de donnée** : si l'assistant conversationnel n'a pas l'information demandée dans les données liées à la procédure active, il répond *"Je ne dispose pas de cette information dans ma base actuelle."* — jamais de réponse générique improvisée.
5. **Mesure du taux d'hallucination** sur le dataset de test (section 33), pour avoir un chiffre réel et pas juste une impression que "ça a l'air de marcher".

## Assistant conversationnel (chat contextuel)

L'assistant (`POST /api/assistant/chat`) fonctionne sur le même principe retrieval-first, mais avec un contexte réduit à **la procédure active de l'utilisateur** (`user_procedures` + tout ce qui s'y rattache). Quand l'utilisateur demande *"Il me manque quoi ?"*, le backend calcule directement la différence entre les documents requis et `user_step_progress`, et transmet ce résultat déjà calculé au LLM pour reformulation — le LLM ne fait jamais le calcul lui-même à partir de rien.

## Multilingue — cas concret

| Message | Langue détectée | intent_code | confidence |
|---|---|---|---|
| "Je veux renouveler mon passeport." | fr | passport_renewal | 0.94 |
| "I need to renew my passport." | en | passport_renewal | 0.93 |
| "أريد تجديد جواز السفر." | ar | passport_renewal | 0.91 |
| "بغيت نجدد الباسبور ديالي." | dar | passport_renewal | 0.78 |
| "bghit ndir l'card d'identité" | dar (latin) | national_id_first_request | 0.65 → clarification |
| "je veux regarder un film" | fr | null | — (hors périmètre) |

Ce tableau illustre bien pourquoi la darija reste, pour l'instant, un périmètre volontairement limité (quelques formulations testées plutôt qu'une couverture large) : la confiance y est structurellement plus basse, ce qui déclenche plus souvent une clarification — un compromis que je préfère largement à une fausse certitude.

## Logs et évaluation

Chaque appel au pipeline écrit une ligne dans `ai_query_logs` (section modèle de données) : texte d'entrée, langue détectée, intent détecté, confiance, procédure associée, temps de réponse. Ces logs servent uniquement à calculer les métriques d'évaluation (intent accuracy, precision, recall, F1-score, taux de clarification, taux d'hallucination mesurable) sur mon dataset de test — jamais à des fins autres que le debugging et l'amélioration du système.

---

## Prochaine étape

L'architecture IA est posée. La suite logique : commencer le développement backend (Phase 3) — authentification, entités JPA à partir du schéma déjà défini, puis endpoints `procedures`. Je n'avance pas sur le service IA tant que le backend ne peut pas au moins servir une procédure depuis la base.
