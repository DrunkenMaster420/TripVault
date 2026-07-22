import { Navigate } from "react-router-dom";
import { getToken } from "../utils/AuthUtils";

interface Props {
  children: React.ReactElement;
}

function ProtectedRoute({ children }: Props) {
  const token = getToken();
  console.log("🔒 [ProtectedRoute] Checking token:", token);

  if (!token) {
    console.error("❌ [ProtectedRoute] No token! Redirecting to /");
    return <Navigate to="/" replace />;
  }

  return children;
}

export default ProtectedRoute;
