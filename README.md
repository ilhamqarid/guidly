# Guidly

> *Comprendre une démarche. Savoir quoi faire. Avancer étape par étape.*

Guidly est un assistant intelligent qui aide les citoyens à comprendre et suivre leurs démarches administratives : recherche en langage naturel (français, arabe, darija), parcours personnalisé étape par étape, checklist de documents, et assistant conversationnel — le tout basé sur une base de données de procédures vérifiées, **jamais générée par l'IA**.

**Statut : MVP fonctionnel — backend, frontend et service IA connectés et opérationnels.**

---

## Le principe central

L'IA ne fabrique jamais une procédure administrative. Elle sert uniquement à :
- **comprendre** une demande écrite en langage libre (français, arabe, darija) ;
- **retrouver** la procédure correspondante dans une base vérifiée ;
- **expliquer** les informations en langage simple.

La base de données relationnelle reste la seule source de vérité — jamais une improvisation du modèle.


<img width="1903" height="915" alt="image" src="https://github.com/user-attachments/assets/7f0cef37-a1b1-4877-92c8-7e14973a2d42" />


---

## Architecture

```
guidly/
├── backend/       Spring Boot (Java) — API principale, auth JWT, base MySQL (port 8080)
├── frontend/       React + Vite — interface utilisateur (port 5173 en dev)
├── ai-service/     FastAPI (Python) — détection d'intention multilingue (port 8000)
├── database/       Schéma SQL et documentation de la base
├── docs/           Analyse fonctionnelle, modèle de données, architecture IA détaillée
└── tests/          Dataset de test IA
```

**Flux d'une requête à l'assistant :**

```
frontend (5173) → backend (8080) → ai-service (8000)
```

Le frontend ne parle jamais directement au service IA — tout passe par le backend, qui va ensuite chercher la vraie procédure en base avant de renvoyer une réponse.

---

## Fonctionnalités

### Côté utilisateur
- Recherche de démarches par mots-clés, avec filtres (organisme, catégorie)
- Assistant conversationnel : détection d'intention en langage naturel (FR/AR/darija), clarification si la demande est ambiguë, réponses contextuelles sur une démarche en cours ("il me manque quoi ?", "quelle est la prochaine étape ?", "pourquoi ce document ?")
- Fiche détaillée de chaque démarche : étapes, documents requis, sources officielles, durée et frais estimés
- Glossaire administratif avec termes cliquables dans les réponses de l'assistant
- Checklist personnelle avec suivi de progression
- Authentification JWT, gestion de compte (modification du profil, changement de mot de passe)

### Côté administrateur
- CRUD complet des procédures (création, modification, archivage — jamais de suppression physique, pour préserver l'historique des utilisateurs)
- Tableau de bord avec statistiques (procédures actives/archivées)
- Consultation des logs de l'assistant IA (requêtes, intentions détectées, taux de confiance)

---

## Technologies

| Couche | Technologie |
|---|---|
| Frontend | React (Vite) |
| Backend | Spring Boot (Java 17), Spring Security, JWT |
| Base de données | MySQL (InnoDB) |
| Service IA | Python + FastAPI, détection d'intention par dictionnaire de mots-clés multilingue |
| Versioning | Git + GitHub |

---

## Documentation détaillée

- [Analyse fonctionnelle](docs/01-analyse-fonctionnelle.md) — problématique, personas, MVP, cas d'utilisation, risques
- [Modèle de données](docs/02-modele-donnees.md) — schéma relationnel complet, script SQL
- [Architecture IA](docs/03-architecture-ia.md) — pipeline de détection d'intention, gestion de la confiance, mécanismes anti-hallucination

---

## Installation

### 1. Base de données
```bash
mysql -u root -p -e "CREATE DATABASE guidly_db CHARACTER SET utf8mb4;"
mysql -u root -p guidly_db < database/schema.sql
```

### 2. ai-service (Python)
```bash
cd ai-service
python -m venv venv
source venv/bin/activate      # Windows : venv\Scripts\activate
pip install -r requirements.txt
uvicorn main:app --reload --port 8000
```
Le service tourne sur `http://localhost:8000`. Documentation interactive auto-générée sur `http://localhost:8000/docs` (Swagger UI).

### 3. backend (Java / Maven)
```bash
cd backend
cp .env.example .env      # puis renseigner JWT_SECRET, DB_PASSWORD, etc.
export $(cat .env | xargs)  # Linux/Mac — sous Windows, définir autrement
./mvnw spring-boot:run
```
Nécessite une base MySQL `guidly_db` accessible (voir `DB_URL` dans `.env`).

### 4. frontend (React)
```bash
cd frontend
cp .env.example .env      # si l'API n'est pas sur localhost:8080
npm install
npm run dev
```
Ouvre `http://localhost:5173`.

---

## API — aperçu

```http
POST /api/auth/register
POST /api/auth/login

POST /api/ai/analyze
POST /api/assistant/chat

GET  /api/procedures
GET  /api/procedures/{id}
GET  /api/procedures/{id}/steps
GET  /api/procedures/{id}/documents
GET  /api/procedures/{id}/sources
GET  /api/procedures/{id}/glossary

POST /api/user-procedures
PUT  /api/user-procedures/{id}/steps/{stepId}

GET  /api/users/me
PUT  /api/users/me
PUT  /api/users/me/password

GET    /api/admin/procedures
POST   /api/admin/procedures
PUT    /api/admin/procedures/{id}
DELETE /api/admin/procedures/{id}
GET    /api/admin/logs
```

---

## Tests

- **ai-service** : dataset de test multilingue (`test_intents.py`) mesurant la précision de détection d'intention, séparément par langue (français, arabe, darija)
- **backend** : structure prête pour tests unitaires et d'intégration (JUnit)

---

## Limites assumées

Guidly est un **outil d'information et d'orientation**, pas un service administratif officiel :
- il ne garantit pas l'acceptation d'un dossier ;
- chaque procédure affiche sa source officielle et sa date de dernière vérification ;
- l'interface n'est pas encore traduite (le champ "langue préférée" existe en base, prêt pour une future internationalisation complète).

## Évolutions futures

- Internationalisation complète de l'interface (FR/AR/darija)
- Page glossaire globale (actuellement le glossaire est consultable par procédure)
- Statistiques admin avancées (procédures les plus recherchées, taux d'abandon)
- Tests automatisés étendus (JUnit côté backend, dataset IA élargi)

---

## Auteure

Ilham Qarid — Étudiante en Génie Informatique, ENSA Fès
