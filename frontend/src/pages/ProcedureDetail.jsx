import { useEffect, useState } from "react";
import { Link, useParams } from "react-router-dom";
import api from "../api/client";
import { useAuth } from "../context/AuthContext";
import FolderIcon from "../components/FolderIcon";
import MetaIcon from "../components/MetaIcon";

export default function ProcedureDetail() {
  const { id } = useParams();
  const { user } = useAuth();

  const [procedure, setProcedure] = useState(null);
  const [steps, setSteps] = useState([]);
  const [documents, setDocuments] = useState([]);
  const [sources, setSources] = useState([]);
  const [tab, setTab] = useState("documents");
  const [message, setMessage] = useState("");

  useEffect(() => {
    api.get(`/procedures/${id}`).then((res) => setProcedure(res.data));
    api.get(`/procedures/${id}/steps`).then((res) => setSteps(res.data));
    api.get(`/procedures/${id}/documents`).then((res) => setDocuments(res.data));
    api.get(`/procedures/${id}/sources`).then((res) => setSources(res.data));
  }, [id]);

  async function handleStart() {
    try {
      await api.post("/user-procedures", { procedureId: Number(id) });
      setMessage("Démarche démarrée ! Retrouve-la dans \"Mes démarches\".");
    } catch (e) {
      setMessage("Erreur au démarrage de la démarche.");
    }
  }

  if (!procedure) return <p className="page">Chargement...</p>;

  return (
    <div className="page procedure-detail-page">
      <Link to="/home" className="back-link">← Retour aux démarches</Link>

      <div className="procedure-header">
        <span className="result-icon large"><FolderIcon size={28} /></span>
        <div className="procedure-header-text">
          <h2>{procedure.title}</h2>
          <p>{procedure.targetUsers || "Tous publics"}</p>
        </div>
        <span className="badge">Vérifié</span>
      </div>

      <div className="meta-box-row">
        <div className="meta-box">
          <span className="meta-box-icon-circle mi-building"><MetaIcon type="building" size={20} /></span>
          <strong>Organisme</strong>
          <p>Organisme concerné</p>
        </div>
        <div className="meta-box">
          <span className="meta-box-icon-circle mi-doc"><MetaIcon type="doc" size={20} /></span>
          <strong>{documents.length} documents</strong>
          <p>à préparer</p>
        </div>
        <div className="meta-box">
          <span className="meta-box-icon-circle mi-steps"><MetaIcon type="steps" size={20} /></span>
          <strong>{steps.length} étapes</strong>
          <p>à suivre</p>
        </div>
        {procedure.estimatedDuration && (
          <div className="meta-box">
            <span className="meta-box-icon-circle mi-clock"><MetaIcon type="clock" size={20} /></span>
            <strong>{procedure.estimatedDuration}</strong>
            <p>Durée estimée</p>
          </div>
        )}
      </div>

      <p>{procedure.description}</p>

      {sources.length > 0 && (
        <div className="verified-banner">
          <span>✓</span>
          <div>
            <strong>Informations officielles et vérifiées</strong>
            <p>Cette démarche est basée sur des sources vérifiées le {sources[0].lastVerifiedAt}.</p>
          </div>
          <button className="link-btn" onClick={() => setTab("sources")}>Voir les sources →</button>
        </div>
      )}

      <div className="detail-tabs">
        <button className={tab === "documents" ? "detail-tab active" : "detail-tab"} onClick={() => setTab("documents")}>Documents</button>
        <button className={tab === "steps" ? "detail-tab active" : "detail-tab"} onClick={() => setTab("steps")}>Étapes</button>
        <button className={tab === "sources" ? "detail-tab active" : "detail-tab"} onClick={() => setTab("sources")}>Sources</button>
      </div>

      {tab === "documents" && (
        <div className="doc-grid">
          {documents.map((d) => (
            <div key={d.documentId} className="doc-card">
              <span className="doc-icon"><MetaIcon type="doc" size={18} /></span>
              <strong>{d.name}</strong>
              <span className={d.required ? "tag-required" : "tag-optional"}>
                {d.required ? "Obligatoire" : "Optionnel"}
              </span>
            </div>
          ))}
          {documents.length === 0 && <p>Aucun document enregistré.</p>}
        </div>
      )}

      {tab === "steps" && (
        <ol className="step-list">
          {steps.map((s) => (
            <li key={s.id}>
              <strong>{s.title}</strong> {!s.required && <span className="badge-optional">optionnel</span>}
              <p>{s.description}</p>
            </li>
          ))}
          {steps.length === 0 && <p>Aucune étape enregistrée.</p>}
        </ol>
      )}

      {tab === "sources" && (
        <ul className="source-list">
          {sources.map((s) => (
            <li key={s.id}>
              <a href={s.url} target="_blank" rel="noreferrer">{s.title}</a>
              <span> — dernière vérification : {s.lastVerifiedAt}</span>
            </li>
          ))}
          {sources.length === 0 && <p>Aucune source enregistrée.</p>}
        </ul>
      )}

      <div className="sticky-action">
        {user ? (
          <button className="btn btn-primary btn-large" onClick={handleStart} style={{ width: "100%" }}>
            Commencer ma démarche
          </button>
        ) : (
          <p><em>Connecte-toi pour suivre ta progression sur cette démarche.</em></p>
        )}
        {message && <p className="info-message">{message}</p>}
      </div>
    </div>
  );
}
