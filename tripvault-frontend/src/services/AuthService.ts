import api from "../api/axios";

export const login = async (username: string, password: string) => {
  const response = await api.post(`/api/auth/login`, {
    username,
    password,
  });

  return response.data;
};

export const register = async (data: RegisterRequest) => {
  const response = await api.post("/api/auth/register", data);
  return response.data;
};

export interface RegisterRequest {
  name: string;
  username: string;
  email: string;
  password: string;
}
