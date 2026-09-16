import axios from "axios";

// En dev, .env (non commité) peut definir VITE_API_URL pour pointer ailleurs
// que localhost. En prod, VITE_API_URL DOIT etre definie au moment du build
// (vite build lit les variables VITE_* de l'environnement), sinon le build
// pointerait vers localhost, ce qui ne fonctionnerait pas une fois déployé.
const api = axios.create({
  baseURL: import.meta.env.VITE_API_URL || "http://localhost:8080/api",
});

api.interceptors.request.use((config) => {
  const token = localStorage.getItem("guidly_token");
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});

export default api;
