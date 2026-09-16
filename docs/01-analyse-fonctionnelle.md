# Guidly — Analyse fonctionnelle

> *Comprendre une démarche. Savoir quoi faire. Avancer étape par étape.*

## Pourquoi ce projet

L'idée m'est venue en observant à quel point une démarche administrative simple peut vite devenir compliquée quand on ne sait pas par où commencer. On connaît son besoin ("je veux m'inscrire à la fac", "je dois refaire mon passeport") mais pas forcément le nom exact de la procédure, ni les documents à préparer, ni l'ordre des étapes. Et souvent, l'information existe quelque part, mais elle est éparpillée entre plusieurs sites ou guichets, écrite dans un langage administratif qui n'est pas toujours évident à comprendre du premier coup.

C'est encore plus vrai dans un contexte comme le Maroc, où une bonne partie des gens s'exprime naturellement en darija plutôt qu'en français — et les outils existants ne prennent quasiment jamais ça en compte.

Le résultat concret de ces difficultés : des dossiers incomplets, des allers-retours inutiles, du temps perdu, et pour certains profils (jeunes bacheliers, primo-demandeurs) un vrai sentiment de ne pas savoir comment s'y prendre.

## Ce que je veux construire

Guidly, c'est un assistant qui prend en entrée une phrase écrite en langage naturel — en français, en anglais, en arabe, ou en darija — et qui aide la personne à comprendre exactement ce qu'elle doit faire. Concrètement, le système doit :

1. comprendre l'intention derrière ce que la personne a écrit ;
2. aller chercher la procédure correspondante dans une base de données que j'ai construite et vérifiée moi-même (l'IA ne l'invente jamais) ;
3. adapter le parcours selon le profil de la personne (étudiant, première demande, renouvellement...) ;
4. présenter les étapes et les documents de façon claire, avec une checklist ;
5. permettre de poser des questions à tout moment pour clarifier un point.

Le principe sur lequel j'ai construit toute l'architecture, c'est que **l'IA sert à comprendre et à expliquer, jamais à inventer**. La base de connaissances reste la seule source de vérité — c'est un choix de conception assumé, parce qu'une IA qui "hallucine" une procédure administrative peut avoir des conséquences réelles pour quelqu'un qui s'y fie.

Autre point important à garder en tête : Guidly n'est pas un service administratif officiel, et je ne veux pas qu'il en donne l'impression. C'est un outil d'orientation qui renvoie systématiquement vers les sources officielles.

## Objectifs

Côté produit, je veux que Guidly réduise concrètement le temps qu'une personne met à comprendre une démarche, qu'il limite les dossiers incomplets grâce à la checklist, et qu'il rende l'information accessible même à quelqu'un qui n'est pas à l'aise avec le langage administratif — y compris en darija.

Côté personnel, ce projet est aussi l'occasion de construire quelque chose de plus abouti qu'un exercice académique classique : une vraie architecture IA réfléchie (pas juste un appel à un chatbot), un cycle complet NLP → base de données → API → frontend, et un projet que je pourrai présenter sereinement en entretien ou en soutenance. Je le développe seule, donc j'ai fait attention à garder un périmètre réaliste plutôt que de viser trop large.

## Qui utilise Guidly

- **L'utilisateur citoyen** : la personne qui cherche à comprendre ou effectuer une démarche.
- **L'administrateur** (moi, dans un premier temps) : gère le contenu — procédures, étapes, documents, sources.
- **Le système IA** : comprend la demande, extrait les informations utiles, personnalise, explique.
- **L'organisme administratif** : n'est pas un utilisateur actif, il est simplement référencé dans chaque procédure comme destinataire de la démarche.

## Personas

Pour ne pas concevoir dans le vide, je me suis appuyée sur quelques profils types :

**Yasmine, 18 ans**, vient d'avoir son bac et veut s'inscrire à l'université. Elle ne connaît ni la procédure ni les documents à fournir, s'exprime souvent en darija, et utilise essentiellement son téléphone.

**Karim, 27 ans**, doit renouveler son passeport avant un voyage. Il est pressé et veut une checklist claire, pas un long texte à lire.

**Fatima, 45 ans**, veut créer une petite entreprise. Elle n'est pas à l'aise avec le jargon administratif et a besoin qu'on lui explique les termes simplement.

**Moi, en tant qu'admin**, j'ai besoin d'un espace simple pour ajouter et tenir à jour les procédures sans y passer des heures.

## Ce que je mets dans le MVP

J'ai fait le tri entre ce qui est vraiment nécessaire pour démontrer le concept et ce qui serait "sympa à avoir mais pas indispensable". Le MVP contient :

- la recherche en langage naturel, en français, anglais et arabe, avec un support basique de la darija ;
- la détection d'intention avec un score de confiance, et une clarification automatique si ce score est trop bas ;
- l'extraction d'informations simples (âge, ville, statut, première demande ou renouvellement) ;
- la récupération de la procédure correspondante dans la base ;
- une personnalisation légère du parcours selon le profil ;
- un parcours étape par étape avec checklist de documents, et un suivi de progression ;
- l'explication des termes administratifs difficiles au clic ;
- un assistant conversationnel, mais strictement limité aux données disponibles dans ma base ;
- l'affichage systématique des sources officielles et de leur date de vérification ;
- un espace admin pour gérer tout ce contenu (CRUD) ;
- quelques statistiques utiles (procédures les plus recherchées, taux de complétion) ;
- l'authentification avec gestion de rôles.

## Ce que je repousse à plus tard

Volontairement laissé hors MVP, pour ne pas me disperser : l'OCR de documents, un RAG plus avancé avec recherche sémantique, la géolocalisation des organismes, les notifications, une vraie application mobile, la saisie vocale, la recommandation de démarches connexes, et des analytics poussés. Ce sont de bonnes idées, mais elles n'apportent pas de valeur suffisante pour justifier le temps qu'elles prendraient à ce stade — je les garde en tête comme évolutions possibles.

## Cas d'utilisation principaux

- Rechercher une démarche en langage libre, et recevoir soit la procédure, soit une demande de clarification si le système n'est pas assez sûr.
- Consulter le parcours complet d'une procédure (étapes, documents, organisme, sources).
- Cocher une étape ou un document comme fait, et voir sa progression mise à jour.
- Cliquer sur un terme administratif pour en avoir une explication simple.
- Poser une question à l'assistant sur la démarche en cours, avec une réponse honnête ("je ne dispose pas de cette information") si la donnée n'existe pas dans ma base.
- Pour l'admin : créer, modifier, désactiver une procédure et ses éléments associés, et consulter les statistiques d'usage.
- S'authentifier avec un rôle utilisateur ou admin.

## Besoins fonctionnels et non fonctionnels

Sur le plan fonctionnel, le système doit gérer la saisie libre, détecter une intention avec un score de confiance, demander une clarification en dessous d'un seuil défini, ne récupérer les procédures que depuis la base structurée, générer un parcours personnalisé, suivre la progression, expliquer les termes, répondre via l'assistant en restant dans les limites des données disponibles, afficher les sources avec leur date de vérification, permettre la gestion complète du contenu côté admin, exposer une API REST documentée, et gérer l'authentification par rôle.

Sur le plan non fonctionnel, ce qui compte le plus pour moi : ne jamais présenter une information non vérifiée comme une certitude, sécuriser correctement l'application (mots de passe hashés, JWT, validation des entrées, CORS bien configuré), garder des temps de réponse raisonnables pour que la démo reste fluide, avoir une interface simple et guidée (pas de mur de texte), un rendu responsive avec une vraie priorité au mobile, un code propre et maintenable (DTO, services, repositories bien séparés), une architecture qui reste extensible si je veux ajouter des procédures ou des langues plus tard, et un minimum de données personnelles stockées.

## Les risques auxquels je fais attention

Le risque principal pour moi, en tant qu'étudiante qui développe seule, c'est de viser trop large et de ne rien terminer proprement — c'est pour ça que je m'impose de rester strictement sur le MVP, avec 5 à 10 procédures bien documentées plutôt qu'un catalogue superficiel.

Le deuxième risque, plus technique, c'est que l'IA invente une information administrative fausse. J'y réponds par l'architecture elle-même : le LLM ne répond jamais à partir de rien, il reçoit toujours les données déjà récupérées en base.

Il y a aussi un risque autour du multilingue, mais il ne pèse pas pareil selon la langue : le français et l'anglais sont bien couverts par les outils NLP existants, donc leur ajout n'est pas un vrai risque technique. C'est surtout la darija qui reste fragile — la détection d'intention y est moins fiable, donc je garde ce périmètre volontairement limité au début, avec quelques formulations testées plutôt qu'une couverture large.

Enfin, je fais attention à ne jamais donner l'impression que Guidly est un service administratif officiel ou qu'il garantit quoi que ce soit sur l'issue d'un dossier — ce serait à la fois trompeur et risqué.

## Architecture générale envisagée

```
                 ┌───────────────────┐
                 │     React Web     │   ← interface utilisateur
                 └─────────┬─────────┘
                           │ HTTPS / REST
                           ▼
                 ┌───────────────────┐
                 │   Spring Boot     │   ← API principale (monolithe modulaire)
                 │      API          │      - auth (JWT)
                 └──────┬─────┬──────┘      - procédures / étapes / documents
                        │     │              - progression utilisateur
              ┌─────────┘     └──────────┐   - CRUD admin
              ▼                          ▼
       ┌──────────────┐          ┌──────────────┐
       │    MySQL     │          │  AI Service  │   ← Python + FastAPI
       │  Procédures  │          │   FastAPI    │      - détection d'intention
       │  Users, etc. │          └──────┬───────┘      - extraction d'entités
       └──────────────┘                 │              - personnalisation
                                         ▼              - explication
                                   ┌────────────┐
                                   │    LLM     │   ← comprend et reformule,
                                   │            │     n'invente jamais une
                                   └────────────┘     procédure
```

J'ai volontairement gardé un backend en monolithe modulaire plutôt que de partir sur des microservices — pour un projet solo, ça n'aurait apporté que de la complexité inutile sans réel bénéfice à ce stade.

**Flux typique d'une requête :**

L'utilisateur écrit sa demande en langage libre → le service IA détecte l'intention et extrait les entités utiles → si la confiance est trop faible, je renvoie une clarification à l'utilisateur → sinon, le backend va chercher la procédure correspondante en base, applique les règles/conditions pertinentes, et le service IA reformule le résultat en langage simple avant affichage.

---

## Prochaine étape

La suite logique, c'est le modèle de données détaillé et l'architecture IA (comment l'intention est détectée précisément, avec des exemples concrets). J'avance étape par étape, sans passer à la suivante tant que l'étape en cours n'est pas claire et fonctionnelle.
