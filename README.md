# Guidly

Projet composé de 3 briques indépendantes qui communiquent entre elles :

```
guidly/
├── backend/      Spring Boot (Java) — API principale, auth JWT, base MySQL (port 8080)
├── frontend/     React + Vite — interface utilisateur (port 5173 en dev)
└── ai-service/   FastAPI (Python) — détection d'intention (NLU) (port 8000)
```

Flux : `frontend (5173) → backend (8080) → ai-service (8000)` pour tout ce qui
concerne l'assistant IA. Le frontend ne parle jamais directement au service IA.

## Lancer le projet en local

### 1. ai-service (Python)
```bash
cd ai-service
python -m venv venv
source venv/bin/activate      # Windows : venv\Scripts\activate
pip install -r requirements.txt
uvicorn main:app --reload --port 8000
```

### 2. backend (Java / Maven)
```bash
cd backend
cp .env.example .env          # puis renseigner JWT_SECRET, DB_PASSWORD, etc.
# charger les variables de .env dans ton environnement avant de lancer, ex :
export $(cat .env | xargs)    # Linux/Mac — sous Windows, les définir autrement
./mvnw spring-boot:run
```
Nécessite une base MySQL `guidly-db` accessible (voir `DB_URL` dans `.env`).

### 3. frontend (React)
```bash
cd frontend
cp .env.example .env          # si l'API n'est pas sur localhost:8080
npm install
npm run dev
```

## Changements récents (corrections de revue de code)

- **backend** : secrets (JWT, mot de passe DB) externalisés en variables
  d'environnement, `@PreAuthorize` ajouté sur les endpoints admin en défense
  en profondeur.
- **frontend** : bug bloquant corrigé (import manquant de `Navbar` qui
  faisait planter toute l'app), routes `/admin`, `/account`,
  `/my-procedures` protégées côté client, URL de l'API configurable.
- **ai-service** : correction du scoring du NLU (les mots-clés génériques
  isolés comme "société" ou "voiture" ne déclenchent plus de fausses
  détections), CORS restreint aux origines de dev connues.
