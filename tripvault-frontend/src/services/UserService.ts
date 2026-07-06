import axios from "axios";
import { getToken } from "../utils/AuthUtils";

const API = "http://localhost:8080";

export const getCurrentUser = async () => {
  const token = getToken();

  const response = await axios.get(`${API}/api/users/me`, {
    headers: {
      Authorization: `Bearer ${token}`,
    },
  });

  return response.data;
};
