import { createContext, useContext, useState } from "react";
import api from "../api/client";

const AuthContext = createContext(null);

export function AuthProvider({ children }) {
  const [user, setUser] = useState(() => {
    const stored = localStorage.getItem("guidly_user");
    return stored ? JSON.parse(stored) : null;
  });

  async function login(email, password) {
    const res = await api.post("/auth/login", { email, password });
    localStorage.setItem("guidly_token", res.data.token);
    localStorage.setItem("guidly_user", JSON.stringify(res.data));
    setUser(res.data);
    return res.data;
  }

  async function register(name, email, password) {
    const res = await api.post("/auth/register", { name, email, password });
    localStorage.setItem("guidly_token", res.data.token);
    localStorage.setItem("guidly_user", JSON.stringify(res.data));
    setUser(res.data);
    return res.data;
  }

  function logout() {
    localStorage.removeItem("guidly_token");
    localStorage.removeItem("guidly_user");
    setUser(null);
  }

  function updateUser(partialUpdate) {
    setUser((prev) => {
      const updated = { ...prev, ...partialUpdate };
      localStorage.setItem("guidly_user", JSON.stringify(updated));
      return updated;
    });
  }

  return (
    <AuthContext.Provider value={{ user, login, register, logout, updateUser }}>
      {children}
    </AuthContext.Provider>
  );
}

export function useAuth() {
  return useContext(AuthContext);
}
