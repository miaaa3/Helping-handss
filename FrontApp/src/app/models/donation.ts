export type DonationStatus = 'PENDING' | 'SUCCEEDED' | 'FAILED' | 'REFUNDED';

export interface Donation {
    id: number;
    amount: number;
    currency: string;
    status: DonationStatus;
    message?: string;
    anonymous: boolean;
    createdAt: string;
    donorId?: number | null;
    donorName: string;
    organizationId: number;
    organizationName: string;
}

export interface CreateDonationRequest {
    organizationId: number;
    amount: number;
    currency?: string;
    message?: string;
    anonymous: boolean;
}

export interface CreateDonationIntentResponse {
    clientSecret: string;
    donationId: string;
}
