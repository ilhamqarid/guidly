import { BrowserRouter, Routes, Route } from "react-router-dom";
import { AuthProvider } from "./context/AuthContext";
import Navbar from "./components/Navbar";
import RequireAuth from "./components/RequireAuth";
import Landing from "./pages/Landing";
import Home from "./pages/Home";
import ProcedureDetail from "./pages/ProcedureDetail";
import Auth from "./pages/Auth";
import MyProcedures from "./pages/MyProcedures";
import AdminDashboard from "./pages/AdminDashboard";
import AssistantChat from "./pages/AssistantChat";
import Account from "./pages/Account";

export default function App() {
  return (
    <AuthProvider>
      <BrowserRouter>
        <Navbar />
        <Routes>
          <Route path="/" element={<Landing />} />
          <Route path="/home" element={<Home />} />
          <Route path="/procedures/:id" element={<ProcedureDetail />} />
          <Route path="/login" element={<Auth />} />
          <Route path="/register" element={<Auth />} />
          <Route
            path="/my-procedures"
            element={
              <RequireAuth>
                <MyProcedures />
              </RequireAuth>
            }
          />
          <Route
            path="/admin"
            element={
              <RequireAuth role="ADMIN">
                <AdminDashboard />
              </RequireAuth>
            }
          />
          <Route path="/assistant" element={<AssistantChat />} />
          <Route
            path="/account"
            element={
              <RequireAuth>
                <Account />
              </RequireAuth>
            }
          />
        </Routes>
      </BrowserRouter>
    </AuthProvider>
  );
}
