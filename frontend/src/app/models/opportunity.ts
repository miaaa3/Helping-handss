export type OpportunityCategory =
  | 'ENVIRONMENT'
  | 'EDUCATION'
  | 'HEALTH'
  | 'ANIMALS'
  | 'COMMUNITY'
  | 'DISASTER_RELIEF'
  | 'OTHER';

export const OPPORTUNITY_CATEGORIES: OpportunityCategory[] = [
  'ENVIRONMENT',
  'EDUCATION',
  'HEALTH',
  'ANIMALS',
  'COMMUNITY',
  'DISASTER_RELIEF',
  'OTHER'
];

export type OpportunityStatus = 'DRAFT' | 'OPEN' | 'FULL' | 'CLOSED';

export const OPPORTUNITY_STATUSES: OpportunityStatus[] = ['DRAFT', 'OPEN', 'FULL', 'CLOSED'];

export interface Opportunity {
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

/** Payload for create/update - only the fields the organization can edit. */
export interface OpportunityRequest {
  title: string;
  description: string;
  category: OpportunityCategory;
  date: string;
  location: string;
  neededVolunteers: number;
  status: OpportunityStatus;
}

/** Filters for the opportunities browse page. */
export interface OpportunityFilters {
  category?: OpportunityCategory;
  status?: OpportunityStatus;
  location?: string;
}
