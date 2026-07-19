import { Route, Routes } from "react-router-dom";

import LandingPage from "./pages/LandingPage";
import LoginPage from "./pages/LoginPage";
import RegisterPage from "./pages/RegisterPage";
import DashboardPage from "./pages/DashboardPage";
import CreateTripPage from "./pages/CreateTripPage";
import TripsDetailPage from "./pages/TripsDetailPage";
import StorageAccountsPage from "./pages/StorageAccountsPage";
import InvitationsPage from "./pages/InvitationsPage";

import ProtectedRoute from "./components/ProtectedRoute";

function App() {
  return (
    <Routes>
      {/* Public */}
      <Route path="/" element={<LandingPage />} />
      <Route path="/login" element={<LoginPage />} />
      <Route path="/register" element={<RegisterPage />} />

      {/* Protected */}
      <Route
        path="/dashboard"
        element={
          <ProtectedRoute>
            <DashboardPage />
          </ProtectedRoute>
        }
      />

      <Route
        path="/create-trip"
        element={
          <ProtectedRoute>
            <CreateTripPage />
          </ProtectedRoute>
        }
      />

      <Route
        path="/trips/:tripId"
        element={
          <ProtectedRoute>
            <TripsDetailPage />
          </ProtectedRoute>
        }
      />

      <Route
        path="/storage-accounts"
        element={
          <ProtectedRoute>
            <StorageAccountsPage />
          </ProtectedRoute>
        }
      />

      <Route
        path="/invitations"
        element={
          <ProtectedRoute>
            <InvitationsPage />
          </ProtectedRoute>
        }
      />
    </Routes>
  );
}

export default App;
