import { useNavigate } from "react-router-dom";
import { useState } from "react";

const EXAMPLES = ["Passeport", "Carte nationale d'identité", "Certificat de résidence", "Créer une entreprise"];

export default function Landing() {
  const [query, setQuery] = useState("");
  const navigate = useNavigate();

  function handleSearch(e) {
    e.preventDefault();
    navigate("/home", { state: { initialQuery: query } });
  }

  return (
    <div className="landing-centered">
      <h1>Les démarches administratives, enfin expliquées simplement.</h1>
      <p className="landing-description">
        Trouve ta démarche et découvre exactement quoi faire, étape par étape,
        sans jargon administratif.
      </p>

      <form onSubmit={handleSearch} className="landing-search landing-search-full">
        <svg className="landing-search-icon" width="18" height="18" viewBox="0 0 24 24" fill="none">
          <circle cx="11" cy="11" r="7" stroke="currentColor" strokeWidth="2" />
          <path d="M21 21l-4.35-4.35" stroke="currentColor" strokeWidth="2" strokeLinecap="round" />
        </svg>
        <input
          type="text"
          placeholder="Ex : renouvellement passeport..."
          value={query}
          onChange={(e) => setQuery(e.target.value)}
        />
        <button type="submit" className="btn btn-primary">Rechercher</button>
      </form>

      <div className="landing-examples landing-examples-centered">
        {EXAMPLES.map((ex) => (
          <button
            key={ex}
            className="chip"
            onClick={() => navigate("/home", { state: { initialQuery: ex } })}
          >
            {ex}
          </button>
        ))}
      </div>

      <div className="trust-row trust-row-centered">
        <div className="trust-item">
          <span className="trust-icon">
            <svg width="20" height="20" viewBox="0 0 24 24" fill="none">
              <path d="M12 2l8 4v6c0 5-3.4 8.4-8 10-4.6-1.6-8-5-8-10V6l8-4z" stroke="currentColor" strokeWidth="1.8" />
              <path d="M9 12l2 2 4-4" stroke="currentColor" strokeWidth="1.8" strokeLinecap="round" strokeLinejoin="round" />
            </svg>
          </span>
          <div>
            <strong>Informations vérifiées</strong>
            <p>Basées sur des sources officielles</p>
          </div>
        </div>
        <div className="trust-item">
          <span className="trust-icon">
            <svg width="20" height="20" viewBox="0 0 24 24" fill="none">
              <path d="M4 6h16M4 12h16M4 18h10" stroke="currentColor" strokeWidth="1.8" strokeLinecap="round" />
            </svg>
          </span>
          <div>
            <strong>Étapes claires</strong>
            <p>En langage simple</p>
          </div>
        </div>
        <div className="trust-item">
          <span className="trust-icon">
            <svg width="20" height="20" viewBox="0 0 24 24" fill="none">
              <circle cx="12" cy="12" r="9" stroke="currentColor" strokeWidth="1.8" />
              <path d="M9 12l2 2 4-4" stroke="currentColor" strokeWidth="1.8" strokeLinecap="round" strokeLinejoin="round" />
            </svg>
          </span>
          <div>
            <strong>Fiable</strong>
            <p>Aucune information inventée</p>
          </div>
        </div>
      </div>
    </div>
  );
}
