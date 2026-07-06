import { Navigate } from "react-router-dom";
import { getToken } from "../utils/AuthUtils";

interface Props {
  children: React.ReactElement;
}

function ProtectedRoute({ children }: Props) {
  const token = getToken();

  if (!token) {
    return <Navigate to="/" />;
  }

  return children;
}

export default ProtectedRoute;
