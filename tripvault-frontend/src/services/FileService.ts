import axios from "axios";
import { getToken } from "../utils/AuthUtils";

const API_BASE_URL = "http://localhost:8080";

export const downloadFile = async (fileId: number) => {
  const token = getToken();

  const response = await axios.get(
    `${API_BASE_URL}/api/files/download/${fileId}`,
    {
      headers: {
        Authorization: `Bearer ${token}`,
      },
      responseType: "blob",
    },
  );

  return response;
};

export const getFiles = async (tripId: number) => {
  const token = getToken();

  const response = await axios.get(`${API_BASE_URL}/api/files/trip/${tripId}`, {
    headers: {
      Authorization: `Bearer ${token}`,
    },
  });

  return response.data;
};

export const deleteFile = async (fileId: number) => {
  const token = getToken();

  await axios.delete(`${API_BASE_URL}/api/files/${fileId}`, {
    headers: {
      Authorization: `Bearer ${token}`,
    },
  });
};

export const uploadFile = async (tripId: number, file: File) => {
  const token = getToken();

  const formData = new FormData();

  formData.append("file", file);
  formData.append("tripId", tripId.toString());

  await axios.post(`${API_BASE_URL}/api/files/upload`, formData, {
    headers: {
      Authorization: `Bearer ${token}`,
    },
  });
};


