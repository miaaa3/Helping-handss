import { OpportunityCategory, OpportunityStatus } from './opportunity';

/** Mirrors backend AdminOrganizationResponse - row shape for the "verify organizations" table. */
export interface AdminOrganization {
  id: number;
  name: string;
  email: string;
  profile: string;
  type: string;
  enabled: boolean;
  verificationStatus: 'PENDING' | 'VERIFIED' | 'REJECTED';
  campaignGoal: number | null;
}

/** Mirrors backend AdminUserResponse - row shape for the "manage users" table. */
export interface AdminUser {
  id: number;
  name: string;
  email: string;
  role: string;
  profile: string;
  enabled: boolean;

  /** Organizations only. */
  verificationStatus?: 'PENDING' | 'VERIFIED' | 'REJECTED';
}

/** Mirrors backend AdminPostResponse - row shape for the content moderation posts table. */
export interface AdminPost {
  id: number;
  content: string;
  createdAt: string;
  authorId: number;
  authorName: string;
  authorProfile: string;
  mediaCount: number;
}

/** Mirrors backend AdminOpportunityResponse-equivalent (reuses OpportunityResponse) - includes drafts. */
export interface AdminOpportunity {
  id: number;
  title: string;
  description: string;
  category: OpportunityCategory;
  date: string;
  location: string;
  neededVolunteers: number;
  applicantCount: number;
  status: OpportunityStatus;
  createdAt: string;
  organizationId: number;
  organizationName: string;
  organizationProfile: string;
}
