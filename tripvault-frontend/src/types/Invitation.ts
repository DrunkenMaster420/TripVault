export interface Invitation {
  id: number;
  tripId: number;
  tripName: string;
  senderName: string;
  allocatedQuota: number;
  createdAt: string;
}
