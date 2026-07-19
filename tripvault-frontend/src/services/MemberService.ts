import api from "../api/axios";

export const getMembers = async (tripId: number) => {
  const response = await api.get(`/api/trips/${tripId}/members`);

  return response.data;
};

export const addMember = async (
  tripId: number,
  userId: number,
  allocatedGB: number,
) => {
  const response = await api.post(`/api/trips/${tripId}/members`, {
    userId,
    allocatedBytes: allocatedGB * 1024 * 1024 * 1024,
  });

  return response.data;
};
