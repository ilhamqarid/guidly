import { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import api from "../api/client";
import DocumentIcon from "../components/DocumentIcon";

export default function MyProcedures() {
  const [procedures, setProcedures] = useState([]);
  const [loading, setLoading] = useState(true);

  function load() {
    api.get("/user-procedures")
      .then((res) => setProcedures(res.data))
      .finally(() => setLoading(false));
  }

  useEffect(load, []);

  async function toggleStep(userProcedureId, stepId, completed) {
    await api.put(`/user-procedures/${userProcedureId}/steps/${stepId}?completed=${!completed}`);
    load();
  }

  if (loading) return <p className="page">Chargement...</p>;

  const inProgress = procedures.filter((p) => p.status === "IN_PROGRESS");
  const completed = procedures.filter((p) => p.status === "COMPLETED");

  return (
    <div className="page">
      <h2>Mes démarches</h2>

      <div className="stats-row">
        <div className="stat-card">
          <span className="stat-icon stat-icon-blue">🏛</span>
          <div>
            <strong>{inProgress.length}</strong>
            <p>En cours</p>
          </div>
        </div>
        <div className="stat-card">
          <span className="stat-icon stat-icon-green">✓</span>
          <div>
            <strong>{completed.length}</strong>
            <p>Terminées</p>
          </div>
        </div>
        <div className="stat-card">
          <span className="stat-icon stat-icon-amber">📋</span>
          <div>
            <strong>{procedures.length}</strong>
            <p>Au total</p>
          </div>
        </div>
      </div>

      {procedures.length === 0 && (
        <p>
          Tu n'as pas encore démarré de démarche.{" "}
          <Link to="/home">Chercher une démarche</Link>
        </p>
      )}

      {inProgress.length > 0 && <h3>En cours</h3>}
      {inProgress.map((up) => (
        <ProcedureProgressCard key={up.id} up={up} onToggleStep={toggleStep} />
      ))}

      {completed.length > 0 && <h3>Terminées</h3>}
      {completed.map((up) => (
        <ProcedureProgressCard key={up.id} up={up} onToggleStep={toggleStep} />
      ))}
    </div>
  );
}

function ProcedureProgressCard({ up, onToggleStep }) {
  const percent = up.totalSteps ? (up.completedSteps / up.totalSteps) * 100 : 0;
  return (
    <div className="procedure-progress-card">
      <div className="procedure-progress-header">
        <span className="result-icon"><DocumentIcon /></span>
        <div>
          <strong>{up.procedureTitle}</strong>
          <p className="progress-label">{up.completedSteps} / {up.totalSteps} étapes terminées</p>
        </div>
        <span className="badge">{up.status === "COMPLETED" ? "Terminée" : "En cours"}</span>
      </div>
      <div className="progress-bar"><div className="progress-bar-fill" style={{ width: `${percent}%` }} /></div>

      <ul className="checklist">
        {up.steps.map((s) => (
          <li key={s.stepId}>
            <input
              type="checkbox"
              checked={s.completed}
              onChange={() => onToggleStep(up.id, s.stepId, s.completed)}
            />
            <span>{s.title}</span>
          </li>
        ))}
      </ul>
    </div>
  );
}
