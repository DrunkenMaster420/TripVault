import api from "../api/axios";

export const getStorageAccounts = async () => {
  const response = await api.get(`/api/storage-accounts`);

  return response.data;
};

export const getGoogleLoginUrl = async () => {
  const response = await api.get(`/api/auth/google/login`);

  return response.data;
};
