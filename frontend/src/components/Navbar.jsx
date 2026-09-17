import { Link, useNavigate } from "react-router-dom";
import { useEffect, useRef, useState } from "react";
import { useAuth } from "../context/AuthContext";

export default function Navbar() {
  const { user, logout } = useAuth();
  const navigate = useNavigate();
  const [menuOpen, setMenuOpen] = useState(false);
  const menuRef = useRef(null);

  useEffect(() => {
    function handleClickOutside(e) {
      if (menuRef.current && !menuRef.current.contains(e.target)) {
        setMenuOpen(false);
      }
    }
    document.addEventListener("mousedown", handleClickOutside);
    return () => document.removeEventListener("mousedown", handleClickOutside);
  }, []);

  function handleLogout() {
    setMenuOpen(false);
    logout();
    navigate("/");
  }

  const initial = user?.name ? user.name.trim().charAt(0).toUpperCase() : "";

  return (
    <nav className="navbar">
      <Link to="/" className="navbar-brand">
        <span className="navbar-logo-icon">G</span>
        Guidly
      </Link>
      <div className="navbar-links">
        <Link to="/">Accueil</Link>
        <Link to="/home">Démarches</Link>
        <Link to="/assistant">Assistant</Link>
        {user && <Link to="/my-procedures">Mes démarches</Link>}

        {user ? (
          <div className="user-menu" ref={menuRef}>
            <button className="user-avatar" onClick={() => setMenuOpen((o) => !o)}>
              {initial}
            </button>
            {menuOpen && (
              <div className="user-dropdown">
                <div className="user-dropdown-header">
                  <strong>{user.name}</strong>
                  <span>{user.email}</span>
                </div>
                <Link to="/account" onClick={() => setMenuOpen(false)}> Mon compte</Link>
                {user.role === "ADMIN" && (
                  <Link to="/admin" onClick={() => setMenuOpen(false)}> Espace admin</Link>
                )}
                <button onClick={handleLogout}> Déconnexion</button>
              </div>
            )}
          </div>
        ) : (
          <Link to="/login" className="btn btn-primary">Se connecter</Link>
        )}
      </div>
    </nav>
  );
}
