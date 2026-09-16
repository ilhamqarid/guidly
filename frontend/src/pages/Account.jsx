import { useEffect, useState } from "react";
import api from "../api/client";
import { useAuth } from "../context/AuthContext";

const LANGUAGES = [
  { value: "fr", label: "Français" },
  { value: "en", label: "English" },
  { value: "ar", label: "العربية" },
  { value: "dar", label: "Darija" },
];

export default function Account() {
  const { user, updateUser } = useAuth();
  const [tab, setTab] = useState("profile");

  const [name, setName] = useState(user?.name || "");
  const [preferredLanguage, setPreferredLanguage] = useState("fr");
  const [profileMessage, setProfileMessage] = useState("");

  const [currentPassword, setCurrentPassword] = useState("");
  const [newPassword, setNewPassword] = useState("");
  const [confirmPassword, setConfirmPassword] = useState("");
  const [passwordMessage, setPasswordMessage] = useState("");

  useEffect(() => {
    api.get("/users/me").then((res) => {
      setName(res.data.name);
      setPreferredLanguage(res.data.preferredLanguage || "fr");
    }).catch(() => {});
  }, []);

  const initial = user?.name ? user.name.trim().charAt(0).toUpperCase() : "";

  async function handleUpdateProfile(e) {
    e.preventDefault();
    setProfileMessage("");
    try {
      await api.put("/users/me", { name, preferredLanguage });
      updateUser({ name, preferredLanguage });
      setProfileMessage("Profil mis à jour avec succès.");
    } catch (err) {
      setProfileMessage("Erreur lors de la mise à jour.");
    }
  }

  async function handleChangePassword(e) {
    e.preventDefault();
    setPasswordMessage("");
    if (newPassword !== confirmPassword) {
      setPasswordMessage("Les deux nouveaux mots de passe ne correspondent pas.");
      return;
    }
    try {
      await api.put("/users/me/password", { currentPassword, newPassword });
      setPasswordMessage("Mot de passe mis à jour avec succès.");
      setCurrentPassword(""); setNewPassword(""); setConfirmPassword("");
    } catch (err) {
      setPasswordMessage(err.response?.data || "Erreur lors du changement de mot de passe.");
    }
  }

  return (
    <div className="page account-page">
      <div className="account-hero">
        <span className="account-avatar-large">{initial}</span>
        <div className="account-hero-text">
          <h2>{user?.name}</h2>
          <span className="account-role-badge">{user?.role === "ADMIN" ? "Administrateur" : "Citoyen"}</span>
          <p>{user?.email}</p>
        </div>
      </div>

      <div className="auth-tabs" style={{ marginTop: "1.5rem" }}>
        <button className={tab === "profile" ? "auth-tab active" : "auth-tab"} onClick={() => setTab("profile")}>Profil</button>
        <button className={tab === "security" ? "auth-tab active" : "auth-tab"} onClick={() => setTab("security")}>Sécurité</button>
      </div>

      {tab === "profile" && (
        <div className="admin-form-card">
          <form onSubmit={handleUpdateProfile} className="form">
            <label>Nom</label>
            <input value={name} onChange={(e) => setName(e.target.value)} required />

            <label>Email</label>
            <input value={user?.email} disabled style={{ background: "var(--bg)", color: "var(--ink-soft)" }} />

            <label>Langue préférée</label>
            <select value={preferredLanguage} onChange={(e) => setPreferredLanguage(e.target.value)}>
              {LANGUAGES.map((l) => <option key={l.value} value={l.value}>{l.label}</option>)}
            </select>

            {profileMessage && (
              <p className={profileMessage.includes("succès") ? "info-message" : "error"}>{profileMessage}</p>
            )}

            <button type="submit" className="btn btn-primary">Enregistrer</button>
          </form>
        </div>
      )}

      {tab === "security" && (
        <div className="admin-form-card">
          <form onSubmit={handleChangePassword} className="form">
            <label>Mot de passe actuel</label>
            <input type="password" value={currentPassword} onChange={(e) => setCurrentPassword(e.target.value)} required />

            <label>Nouveau mot de passe</label>
            <input type="password" value={newPassword} onChange={(e) => setNewPassword(e.target.value)} required minLength={6} />

            <label>Confirmer le nouveau mot de passe</label>
            <input type="password" value={confirmPassword} onChange={(e) => setConfirmPassword(e.target.value)} required minLength={6} />

            {passwordMessage && (
              <p className={passwordMessage.includes("succès") ? "info-message" : "error"}>{passwordMessage}</p>
            )}

            <button type="submit" className="btn btn-primary">Mettre à jour le mot de passe</button>
          </form>
        </div>
      )}
    </div>
  );
}
