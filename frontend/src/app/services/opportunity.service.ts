import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';
import { Opportunity, OpportunityFilters, OpportunityRequest } from '../models/opportunity';

@Injectable({
  providedIn: 'root'
})
export class OpportunityService {
  private opportunitiesUrl: string;

  constructor(private http: HttpClient) {
    this.opportunitiesUrl = `${environment.apiUrl}api/opportunities`;
  }

  /** Browse/filter opportunities. Without a status filter, drafts are hidden. */
  search(filters: OpportunityFilters = {}): Observable<Opportunity[]> {
    let params = new HttpParams();
    if (filters.category) {
      params = params.set('category', filters.category);
    }
    if (filters.status) {
      params = params.set('status', filters.status);
    }
    if (filters.location) {
      params = params.set('location', filters.location);
    }
    return this.http.get<Opportunity[]>(this.opportunitiesUrl, { params });
  }

  getById(opportunityId: number): Observable<Opportunity> {
    return this.http.get<Opportunity>(`${this.opportunitiesUrl}/${opportunityId}`);
  }

  /** Opportunities posted by one organization. Drafts are only included if the requester owns the organization. */
  getByOrganization(organizationId: number): Observable<Opportunity[]> {
    return this.http.get<Opportunity[]>(`${this.opportunitiesUrl}/organization/${organizationId}`);
  }

  /** Opportunities from organizations the current user follows - for feed cards. */
  /** Personalized opportunity recommendations for the logged-in volunteer. */
  getRecommended(): Observable<Opportunity[]> {
    return this.http.get<Opportunity[]>(`${this.opportunitiesUrl}/recommended`);
  }

  /** Organizations to follow, matched to the volunteer's interests. */
  getSuggestedOrganizations(): Observable<SuggestedOrg[]> {
    return this.http.get<SuggestedOrg[]>(`${this.opportunitiesUrl}/suggested-organizations`);
  }

  getForFeed(userId: number): Observable<Opportunity[]> {
    return this.http.get<Opportunity[]>(`${this.opportunitiesUrl}/feed`, {
      params: new HttpParams().set('userId', userId)
    });
  }

  create(request: OpportunityRequest): Observable<Opportunity> {
    return this.http.post<Opportunity>(this.opportunitiesUrl, request);
  }

  update(opportunityId: number, request: OpportunityRequest): Observable<Opportunity> {
    return this.http.put<Opportunity>(`${this.opportunitiesUrl}/${opportunityId}`, request);
  }

  delete(opportunityId: number): Observable<void> {
    return this.http.delete<void>(`${this.opportunitiesUrl}/${opportunityId}`);
  }
}

export interface SuggestedOrg {
  id: number;
  name: string;
  profile: string;
  type: string;
  description: string;
}
