import api from "../api/axios";

export const inviteMember = async (
  tripId: number,
  username: string,
  allocatedStorage: number,
) => {
  const response = await api.post(`/api/invitations/trips/${tripId}`, {
    username,
    allocatedStorage,
  });

  return response.data;
};

export const getMyInvitations = async () => {
  const response = await api.get("/api/invitations");

  return response.data;
};

export const acceptInvitation = async (id: number) => {
  const response = await api.post(`/api/invitations/${id}/accept`);

  return response.data;
};

export const rejectInvitation = async (id: number) => {
  const response = await api.post(`/api/invitations/${id}/reject`);

  return response.data;
};

export const getInvitationCount = async () => {
  const response = await api.get("/api/invitations/count");
  return response.data;
};
