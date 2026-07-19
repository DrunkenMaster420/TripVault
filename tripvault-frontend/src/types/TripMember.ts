export interface TripMember {
  userId: number;
  username: string;
  name: string;
  role: string;
  allocatedBytes?: number | null;
  usedBytes?: number | null;
  joinedAt?: string | null;
  isActive?: boolean | null;
}
