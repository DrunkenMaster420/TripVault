import axios from "axios";
import { getToken } from "../utils/AuthUtils";

const api = axios.create({
  baseURL: "/",
});

api.interceptors.request.use((config) => {
  const publicEndpoints = ["/api/auth/login", "/api/auth/register"];

  if (!publicEndpoints.includes(config.url ?? "")) {
    const token = getToken();

    if (token && config.headers) {
      config.headers.Authorization = `Bearer ${token}`;
    }
  }

  return config;
});

export default api;
