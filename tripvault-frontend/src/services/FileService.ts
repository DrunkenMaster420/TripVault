import api from "../api/axios";

export const downloadFile = async (fileId: number) => {
  const response = await api.get(`/api/files/download/${fileId}`, {
    responseType: "blob",
  });

  return response;
};

export const getFiles = async (tripId: number) => {
  const response = await api.get(`/api/files/trip/${tripId}`);

  return response.data;
};

export const deleteFile = async (fileId: number) => {
  await api.delete(`/api/files/${fileId}`);
};

export const uploadFile = async (tripId: number, file: File) => {
  const formData = new FormData();

  formData.append("file", file);
  formData.append("tripId", tripId.toString());

  await api.post(`/api/files/upload`, formData, {});
};
