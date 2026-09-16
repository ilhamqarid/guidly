import { Navigate } from "react-router-dom";
import { useAuth } from "../context/AuthContext";

/**
 * Protège une route côté client : redirige vers /login si personne n'est
 * connecté, ou vers l'accueil si un rôle précis est requis et que
 * l'utilisateur connecté ne l'a pas.
 *
 * Ceci est une protection d'UX (éviter d'afficher une page vide/cassée) —
 * la vraie sécurité reste imposée par le backend (SecurityConfig +
 * @PreAuthorize). Ne JAMAIS considérer ce composant comme une mesure de
 * sécurité suffisante à lui seul.
 *
 * Usage :
 *   <RequireAuth><Account /></RequireAuth>
 *   <RequireAuth role="ADMIN"><AdminDashboard /></RequireAuth>
 */
export default function RequireAuth({ children, role }) {
  const { user } = useAuth();

  if (!user) {
    return <Navigate to="/login" replace />;
  }

  if (role && user.role !== role) {
    return <Navigate to="/" replace />;
  }

  return children;
}
