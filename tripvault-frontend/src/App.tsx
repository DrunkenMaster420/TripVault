import { Route, Routes } from "react-router-dom"
import LoginPage from "./pages/LoginPage"
import DashboardPage from "./pages/DashboardPage";
import ProtectedRoute from "./components/ProtectedRoute"
import CreateTripPage from "./pages/CreateTripPage";
import TripsDetailPage from "./pages/TripsDetailPage";
import StorageAccountsPage from "./pages/StorageAccountsPage";

function App() {
    return (
      <Routes>
        <Route path="/" element={<LoginPage />} />
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
      </Routes>
    );
}

export default App