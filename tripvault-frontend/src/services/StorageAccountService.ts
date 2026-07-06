import axios from "axios";
import { getToken } from "../utils/AuthUtils";

const API_BASE_URL = "http://localhost:8080";

export const getStorageAccounts = async () => {
  const token = getToken();

  const response = await axios.get(`${API_BASE_URL}/api/storage-accounts`, {
    headers: {
      Authorization: `Bearer ${token}`,
    },
  });

  return response.data;
};

export const getGoogleLoginUrl = async () => {
  const token = getToken();

  const response = await axios.get(`${API_BASE_URL}/api/auth/google/login`, {
    headers: {
      Authorization: `Bearer ${token}`,
    },
  });

  return response.data;
};
