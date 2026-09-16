import { useEffect, useMemo, useState } from "react";
import { Link, useLocation } from "react-router-dom";
import api from "../api/client";
import MetaIcon from "../components/MetaIcon";
import "./Home.css";

function Home() {
  const location = useLocation();

  const [procedures, setProcedures] = useState([]);
  const [categories, setCategories] = useState([]);
  const [organizations, setOrganizations] = useState([]);
  const [search, setSearch] = useState(location.state?.initialQuery || "");
  const [selectedOrgs, setSelectedOrgs] = useState([]);
  const [selectedCats, setSelectedCats] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    loadAll();
  }, []);

  const loadAll = async () => {
    try {
      const [procRes, catRes, orgRes] = await Promise.all([
        api.get("/procedures"),
        api.get("/categories"),
        api.get("/organizations"),
      ]);

      setCategories(catRes.data);
      setOrganizations(orgRes.data);

      const enriched = await Promise.all(
        procRes.data.map(async (p) => {
          const [steps, docs] = await Promise.all([
            api.get(`/procedures/${p.id}/steps`),
            api.get(`/procedures/${p.id}/documents`),
          ]);
          return { ...p, stepsCount: steps.data.length, documentsCount: docs.data.length };
        })
      );

      setProcedures(enriched);
    } catch (error) {
      console.error("Erreur :", error);
    } finally {
      setLoading(false);
    }
  };

  const catMap = useMemo(() => Object.fromEntries(categories.map((c) => [c.id, c.name])), [categories]);
  const orgMap = useMemo(() => Object.fromEntries(organizations.map((o) => [o.id, o.name])), [organizations]);

  function toggleOrg(id) {
    setSelectedOrgs((prev) => (prev.includes(id) ? prev.filter((x) => x !== id) : [...prev, id]));
  }

  function toggleCat(id) {
    setSelectedCats((prev) => (prev.includes(id) ? prev.filter((x) => x !== id) : [...prev, id]));
  }

  const filtered = procedures.filter((p) => {
    const matchesSearch = (p.title + " " + p.description).toLowerCase().includes(search.toLowerCase());
    const matchesOrg = selectedOrgs.length === 0 || selectedOrgs.includes(p.organizationId);
    const matchesCat = selectedCats.length === 0 || selectedCats.includes(p.categoryId);
    return matchesSearch && matchesOrg && matchesCat;
  });

  return (
    <main className="content">

      {/* SEARCH */}
      <div className="search-container">
        <div className="search-box">
          <span>⌕</span>
          <input
            type="text"
            placeholder="Ex : renouvellement passeport"
            value={search}
            onChange={(e) => setSearch(e.target.value)}
          />
        </div>
        <button className="search-button" onClick={loadAll}>
          Rechercher
        </button>
      </div>

      <div className="result-count">
        {loading ? "Chargement..." : `${filtered.length} résultat${filtered.length !== 1 ? "s" : ""} trouvé${filtered.length !== 1 ? "s" : ""}`}
      </div>

      <div className="layout">

        {/* FILTERS */}
        <aside className="filters">
          <h2>Filtres</h2>

          <h3>ORGANISME</h3>
          {organizations.map((o) => (
            <label key={o.id}>
              <input
                type="checkbox"
                checked={selectedOrgs.includes(o.id)}
                onChange={() => toggleOrg(o.id)}
              />
              {o.name}
            </label>
          ))}

          <h3>CATÉGORIE</h3>
          {categories.map((cat) => (
            <label key={cat.id}>
              <input
                type="checkbox"
                checked={selectedCats.includes(cat.id)}
                onChange={() => toggleCat(cat.id)}
              />
              {cat.name}
            </label>
          ))}
        </aside>

        {/* RESULTS */}
        <section className="results">
          {filtered.map((procedure) => (
            <Link to={`/procedures/${procedure.id}`} className="procedure-card" key={procedure.id}>
              <div className="procedure-icon">📁</div>

              <div className="procedure-content">
                <div className="procedure-header">
                  <div>
                    <h2>{procedure.title}</h2>
                    <p className="category">{catMap[procedure.categoryId] || ""}</p>
                  </div>

                  {procedure.status === "PUBLISHED" && (
                    <span className="verified">✓ Vérifié</span>
                  )}
                </div>

                <div className="procedure-info">
                  <span><MetaIcon type="building" size={15} /> {orgMap[procedure.organizationId] || "Organisme"}</span>
                  <span><MetaIcon type="doc" size={15} /> {procedure.documentsCount} documents</span>
                  <span><MetaIcon type="steps" size={15} /> {procedure.stepsCount} étapes</span>
                  {procedure.estimatedDuration && <span><MetaIcon type="clock" size={15} /> {procedure.estimatedDuration}</span>}
                </div>
              </div>

              <div className="arrow">→</div>
            </Link>
          ))}
          {!loading && filtered.length === 0 && <p>Aucune démarche ne correspond à ta recherche.</p>}
        </section>

      </div>
    </main>
  );
}

export default Home;
