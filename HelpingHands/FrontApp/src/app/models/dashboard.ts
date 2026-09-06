import { Application } from './application';
import { Opportunity } from './opportunity';

/** Summary data for the volunteer dashboard. */
export interface VolunteerDashboard {
  pendingApplications: number;
  acceptedApplications: number;
  rejectedApplications: number;
  completedOpportunities: number;
  unreadMessages: number;
  applications: Application[];
  upcomingOpportunities: Opportunity[];
}

/** Summary data for the organization dashboard. */
export interface OrganizationDashboard {
  opportunities: Opportunity[];
  pendingApplicantsCount: number;
  unreadMessages: number;
  totalRaised: number;
  followerCount: number;
  postCount: number;
}
