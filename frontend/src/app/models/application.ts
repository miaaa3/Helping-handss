import { Opportunity } from './opportunity';

export type ApplicationStatus = 'PENDING' | 'ACCEPTED' | 'REJECTED' | 'CANCELLED';

/** A volunteer's application to an opportunity, with enough context to render either dashboard. */
export interface Application {
  id: number;
  status: ApplicationStatus;
  createdAt: string;
  opportunity: Opportunity;
  volunteerId: number;
  volunteerName: string;
  volunteerProfile: string;
  volunteerEmail: string;
}
