import axios from "axios";
import { getToken } from "../utils/AuthUtils";

// Reads VITE_API_URL from Vercel env, falls back to local dev or relative path
const API_BASE_URL = import.meta.env.VITE_API_URL || "http://localhost:8080";

const api = axios.create({
  baseURL: API_BASE_URL,
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
