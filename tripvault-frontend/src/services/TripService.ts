import api from "../api/axios";

export const getTrips = async () => {
  const response = await api.get("/api/trips");

  return response.data;
};

export const createTrip = async (name: string, description: string) => {
  const response = await api.post(`/api/trips`, { name, description });

  return response.data;
};

export const getTripById = async (tripId: number) => {
  const response = await api.get(`/api/trips/${tripId}`);
  return response.data;
};
