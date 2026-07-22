import axios from "axios";
import { getToken, removeToken } from "../utils/AuthUtils";

// Reads VITE_API_URL from Vercel env, falls back to local dev or relative path
const API_BASE_URL = import.meta.env.VITE_API_URL || "http://localhost:8080";

const api = axios.create({
  baseURL: API_BASE_URL,
});

api.interceptors.request.use((config) => {
  const token = getToken();

  // Attach token to ALL requests if a token exists
  if (token && config.headers) {
    config.headers.Authorization = `Bearer ${token}`;
  }

  return config;
});

api.interceptors.response.use(
  (response) => response,
  (error) => {
    console.error(
      "🚨 [Axios Interceptor] Error:",
      error.response?.status,
      error.message,
    );
    if (error.response?.status === 401) {
      console.error(
        "💥 [Axios Interceptor] 401 Unauthorized detected! Clearing token...",
      );
      removeToken();
      window.location.href = "/";
    }
    return Promise.reject(error);
  },
);

export default api;
