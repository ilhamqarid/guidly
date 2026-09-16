import { useEffect, useState } from "react";
import api from "../api/client";
import { useAuth } from "../context/AuthContext";

const emptyForm = {
  title: "", intentCode: "", categoryId: "", organizationId: "",
  description: "", targetUsers: "", estimatedDuration: "", estimatedFees: "",
  status: "DRAFT", lastVerifiedAt: "",
};

export default function AdminDashboard() {
  const [procedures, setProcedures] = useState([]);
  const [categories, setCategories] = useState([]);
  const [organizations, setOrganizations] = useState([]);
  const [form, setForm] = useState(emptyForm);
  const [editingId, setEditingId] = useState(null);
  const [message, setMessage] = useState("");
  const [showForm, setShowForm] = useState(false);
  const [activeNav, setActiveNav] = useState("procedures");
  const [logs, setLogs] = useState([]);
  const { user } = useAuth();

  function loadAll() {
    api.get("/procedures").then((res) => setProcedures(res.data));
    api.get("/categories").then((res) => setCategories(res.data));
    api.get("/organizations").then((res) => setOrganizations(res.data));
  }

  useEffect(loadAll, []);

  useEffect(() => {
    if (activeNav === "logs") {
      api.get("/admin/logs").then((res) => setLogs(res.data));
    }
  }, [activeNav]);

  function handleChange(e) {
    setForm({ ...form, [e.target.name]: e.target.value });
  }

  async function handleSubmit(e) {
    e.preventDefault();
    setMessage("");
    const payload = {
      ...form,
      categoryId: Number(form.categoryId),
      organizationId: Number(form.organizationId),
    };
    try {
      if (editingId) {
        await api.put(`/admin/procedures/${editingId}`, payload);
        setMessage("Procédure mise à jour.");
      } else {
        await api.post("/admin/procedures", payload);
        setMessage("Procédure créée.");
      }
      setForm(emptyForm);
      setEditingId(null);
      setShowForm(false);
      loadAll();
    } catch (err) {
      setMessage("Erreur : " + (err.response?.data || "vérifie les champs (accès ADMIN requis)."));
    }
  }

  function handleEdit(p) {
    setEditingId(p.id);
    setForm({
      title: p.title, intentCode: p.intentCode,
      categoryId: p.categoryId, organizationId: p.organizationId,
      description: p.description, targetUsers: p.targetUsers || "",
      estimatedDuration: p.estimatedDuration || "", estimatedFees: p.estimatedFees || "",
      status: p.status, lastVerifiedAt: p.lastVerifiedAt,
    });
    setShowForm(true);
  }

  async function handleArchive(id) {
    if (!confirm("Archiver cette procédure ?")) return;
    await api.delete(`/admin/procedures/${id}`);
    loadAll();
  }

  const total = procedures.length;
  const published = procedures.filter((p) => p.status === "PUBLISHED").length;
  const archived = procedures.filter((p) => p.status === "ARCHIVED").length;

  const statusBadgeClass = (status) => {
    if (status === "PUBLISHED") return "badge";
    if (status === "ARCHIVED") return "badge badge-hard";
    return "badge-optional";
  };
  const statusLabel = (status) => {
    if (status === "PUBLISHED") return "Active";
    if (status === "ARCHIVED") return "Archivée";
    return "Brouillon";
  };

  return (
    <div className="admin-layout">
      <aside className="admin-sidebar">
        <div className="admin-sidebar-brand">Guidly <span>Admin</span></div>
        <nav>
          <button className={activeNav === "dashboard" ? "admin-nav-item active" : "admin-nav-item"} onClick={() => setActiveNav("dashboard")}>📊 Dashboard</button>
          <button className={activeNav === "procedures" ? "admin-nav-item active" : "admin-nav-item"} onClick={() => setActiveNav("procedures")}>📁 Procédures</button>
          <button className={activeNav === "logs" ? "admin-nav-item active" : "admin-nav-item"} onClick={() => setActiveNav("logs")}>🧾 Logs</button>
        </nav>
      </aside>

      <main className="admin-main">
        <h2>{activeNav === "logs" ? "Logs de l'assistant IA" : "Dashboard"}</h2>

        {activeNav !== "logs" && (
          <div className="stats-row">
            <div className="stat-card">
              <span className="stat-icon stat-icon-blue">📁</span>
              <div><strong>{total}</strong><p>Procédures</p></div>
            </div>
            <div className="stat-card">
              <span className="stat-icon stat-icon-green">✓</span>
              <div><strong>{published}</strong><p>Actives</p></div>
            </div>
            <div className="stat-card">
              <span className="stat-icon stat-icon-amber">🗄</span>
              <div><strong>{archived}</strong><p>Archivées</p></div>
            </div>
          </div>
        )}

        {activeNav === "logs" ? (
          <table className="admin-table">
            <thead>
              <tr><th>Message</th><th>Langue</th><th>Intention détectée</th><th>Confiance</th><th>Procédure trouvée</th><th>Temps (ms)</th><th>Date</th></tr>
            </thead>
            <tbody>
              {logs.map((log) => (
                <tr key={log.id}>
                  <td>{log.inputText}</td>
                  <td>{log.detectedLanguage || "—"}</td>
                  <td>{log.detectedIntent || "—"}</td>
                  <td>{log.confidence != null ? log.confidence.toFixed(2) : "—"}</td>
                  <td>{log.matchedProcedureTitle || "—"}</td>
                  <td>{log.responseTimeMs ?? "—"}</td>
                  <td>{log.createdAt ? log.createdAt.replace("T", " ").slice(0, 16) : "—"}</td>
                </tr>
              ))}
              {logs.length === 0 && (
                <tr><td colSpan={7}>Aucune requête enregistrée pour l'instant.</td></tr>
              )}
            </tbody>
          </table>
        ) : (
        <>
        <div className="admin-section-header">
          <h3>Procédures récemment modifiées</h3>
          <button className="btn btn-primary" onClick={() => { setShowForm(!showForm); setEditingId(null); setForm(emptyForm); }}>
            {showForm ? "Fermer" : "+ Nouvelle procédure"}
          </button>
        </div>

        {showForm && (
          <form onSubmit={handleSubmit} className="form admin-form-card">
            <label>Titre</label>
            <input name="title" value={form.title} onChange={handleChange} required />

            <label>Intent code</label>
            <input name="intentCode" value={form.intentCode} onChange={handleChange} required />

            <label>Catégorie</label>
            <select name="categoryId" value={form.categoryId} onChange={handleChange} required>
              <option value="">-- choisir --</option>
              {categories.map((c) => <option key={c.id} value={c.id}>{c.name}</option>)}
            </select>

            <label>Organisme</label>
            <select name="organizationId" value={form.organizationId} onChange={handleChange} required>
              <option value="">-- choisir --</option>
              {organizations.map((o) => <option key={o.id} value={o.id}>{o.name}</option>)}
            </select>

            <label>Description</label>
            <textarea name="description" value={form.description} onChange={handleChange} required />

            <label>Public concerné</label>
            <input name="targetUsers" value={form.targetUsers} onChange={handleChange} />

            <label>Durée estimée</label>
            <input name="estimatedDuration" value={form.estimatedDuration} onChange={handleChange} />

            <label>Frais estimés</label>
            <input name="estimatedFees" value={form.estimatedFees} onChange={handleChange} />

            <label>Statut</label>
            <select name="status" value={form.status} onChange={handleChange}>
              <option value="DRAFT">DRAFT</option>
              <option value="PUBLISHED">PUBLISHED</option>
              <option value="ARCHIVED">ARCHIVED</option>
            </select>

            <label>Dernière vérification</label>
            <input type="date" name="lastVerifiedAt" value={form.lastVerifiedAt} onChange={handleChange} required />

            <button type="submit" className="btn btn-primary">
              {editingId ? "Mettre à jour" : "Créer la procédure"}
            </button>
          </form>
        )}

        <table className="admin-table">
          <thead>
            <tr><th>Titre</th><th>Statut</th><th>Dernière modification</th><th>Actions</th></tr>
          </thead>
          <tbody>
            {procedures.map((p) => (
              <tr key={p.id}>
                <td>{p.title}</td>
                <td><span className={statusBadgeClass(p.status)}>{statusLabel(p.status)}</span></td>
                <td>{p.updatedAt ? p.updatedAt.split("T")[0] : "—"}</td>
                <td>
                  <button className="btn btn-outline" onClick={() => handleEdit(p)}>Modifier</button>
                  <button className="btn btn-outline" onClick={() => handleArchive(p.id)}>Archiver</button>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
        </>
        )}
      </main>
    </div>
  );
}
