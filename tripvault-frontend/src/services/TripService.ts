import axios from "axios";
import { getToken } from "../utils/AuthUtils";

const API_BASE_URL = "http://localhost:8080";

export const getTrips = async () => {
    const token = getToken();

    const response = await axios.get(`${API_BASE_URL}/api/trips`, {
        headers: {
            Authorization: `Bearer ${token}`,
        },
    }
    );

    return response.data;
}

export const createTrip = async (name: string, description: string) => {
    const token = getToken();
    const response = await axios.post(`${API_BASE_URL}/api/trips`,
        { name, description },
        { headers: { Authorization: `Bearer ${token}`, }, });

    return response.data;
}

export const getTripById = async (tripId: number) => {
    const token = getToken();
    const response=await axios.get(`${API_BASE_URL}/api/trips/${tripId}`, {
        headers: {
            Authorization: `Bearer ${token}`,
        },
    });
    return response.data;
}
