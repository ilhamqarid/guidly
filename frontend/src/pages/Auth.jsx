import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { useAuth } from "../context/AuthContext";

export default function Auth() {
  const { login, register } = useAuth();
  const navigate = useNavigate();
  const [mode, setMode] = useState("login");
  const [showPassword, setShowPassword] = useState(false);

  const [name, setName] = useState("");
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [error, setError] = useState("");

  async function handleSubmit(e) {
    e.preventDefault();
    setError("");
    try {
      if (mode === "login") {
        await login(email, password);
      } else {
        await register(name, email, password);
      }
      navigate("/home");
    } catch (err) {
      setError(
        mode === "login"
          ? "Email ou mot de passe incorrect."
          : (err.response?.data || "Erreur lors de l'inscription.")
      );
    }
  }

  return (
    <div className="auth-wave-page">
      <div className="auth-wave-topbar">
        {mode === "login" ? (
          <span>Pas encore de compte ? <button onClick={() => setMode("register")}>S'inscrire</button></span>
        ) : (
          <span>Déjà un compte ? <button onClick={() => setMode("login")}>Se connecter</button></span>
        )}
      </div>

      <div className="auth-wave-card">
        <h2>{mode === "login" ? "Connexion" : "Créer un compte"}</h2>
        <p className="auth-wave-subtitle">
          {mode === "login" ? "Continue tes démarches là où tu les as laissées." : "Rejoins Guidly pour suivre tes démarches."}
        </p>

        <form onSubmit={handleSubmit}>
          {mode === "register" && (
            <>
              <label>Nom</label>
              <div className="input-icon-wrap">
                <svg width="18" height="18" viewBox="0 0 24 24" fill="none"><path d="M12 12a4 4 0 100-8 4 4 0 000 8zM4 21c0-4 3.6-7 8-7s8 3 8 7" stroke="currentColor" strokeWidth="1.6" strokeLinecap="round" strokeLinejoin="round" /></svg>
                <input type="text" placeholder="Ton nom" value={name} onChange={(e) => setName(e.target.value)} required />
              </div>
            </>
          )}

          <label>Email</label>
          <div className="input-icon-wrap">
            <svg width="18" height="18" viewBox="0 0 24 24" fill="none"><rect x="3" y="5" width="18" height="14" rx="2" stroke="currentColor" strokeWidth="1.6" /><path d="M3 7l9 6 9-6" stroke="currentColor" strokeWidth="1.6" strokeLinecap="round" strokeLinejoin="round" /></svg>
            <input type="email" placeholder="Entrer ton adresse email" value={email} onChange={(e) => setEmail(e.target.value)} required />
          </div>

          <label>Mot de passe</label>
          <div className="input-icon-wrap">
            <svg width="18" height="18" viewBox="0 0 24 24" fill="none"><rect x="5" y="11" width="14" height="9" rx="2" stroke="currentColor" strokeWidth="1.6" /><path d="M8 11V7a4 4 0 118 0v4" stroke="currentColor" strokeWidth="1.6" /></svg>
            <input
              type={showPassword ? "text" : "password"}
              placeholder="Entrer ton mot de passe"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              required
              minLength={mode === "register" ? 6 : undefined}
            />
            <button type="button" className="eye-toggle" onClick={() => setShowPassword(!showPassword)} aria-label="Afficher/masquer le mot de passe">
              {showPassword ? "🙈" : "👁"}
            </button>
          </div>

          {error && <p className="error">{String(error)}</p>}

          <button type="submit" className="btn btn-primary auth-wave-submit">
            {mode === "login" ? "Se connecter" : "Créer mon compte"}
          </button>
        </form>
      </div>
    </div>
  );
}
