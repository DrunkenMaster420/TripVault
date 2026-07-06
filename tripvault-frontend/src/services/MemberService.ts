import axios from "axios";
import { getToken } from "../utils/AuthUtils";

const API_BASE_URL = "http://localhost:8080";

export const getMembers = async (tripId: number) => {
  const token = getToken();

  const response = await axios.get(
    `${API_BASE_URL}/api/trips/${tripId}/members`,
    {
      headers: {
        Authorization: `Bearer ${token}`,
      },
    },
  );

  return response.data;
};

export const addMember = async (
  tripId: number,
  userId: number,
  allocatedGB: number,
) => {
  const token = getToken();

  const response = await axios.post(
    `${API_BASE_URL}/api/trips/${tripId}/members`,
    {
      userId,
      allocatedBytes: allocatedGB * 1024 * 1024 * 1024,
    },
    {
      headers: {
        Authorization: `Bearer ${token}`,
      },
    },
  );

  return response.data;
};

