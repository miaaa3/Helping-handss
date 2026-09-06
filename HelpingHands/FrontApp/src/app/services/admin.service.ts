import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';
import { AdminOpportunity, AdminOrganization, AdminPost, AdminUser } from '../models/admin';
import { Donation } from '../models/donation';
import { PageResponse } from '../models/page';

@Injectable({
  providedIn: 'root'
})
export class AdminService {
  private adminUrl: string;

  constructor(private http: HttpClient) {
    this.adminUrl = `${environment.apiUrl}api/admin`;
  }

  listOrganizations(): Observable<AdminOrganization[]> {
    return this.http.get<AdminOrganization[]>(`${this.adminUrl}/organizations`);
  }

  setOrganizationVerification(organizationId: number, status: 'PENDING' | 'VERIFIED' | 'REJECTED'): Observable<AdminOrganization> {
    return this.http.patch<AdminOrganization>(`${this.adminUrl}/organizations/${organizationId}/verification`, { status });
  }

  listUsers(): Observable<AdminUser[]> {
    return this.http.get<AdminUser[]>(`${this.adminUrl}/users`);
  }

  setUserEnabled(userId: number, enabled: boolean): Observable<AdminUser> {
    return this.http.patch<AdminUser>(`${this.adminUrl}/users/${userId}/status`, { enabled });
  }

  listPosts(page = 0, size = 20): Observable<PageResponse<AdminPost>> {
    const params = new HttpParams().set('page', page).set('size', size);
    return this.http.get<PageResponse<AdminPost>>(`${this.adminUrl}/posts`, { params });
  }

  deletePost(postId: number): Observable<void> {
    return this.http.delete<void>(`${this.adminUrl}/posts/${postId}`);
  }

  listOpportunities(): Observable<AdminOpportunity[]> {
    return this.http.get<AdminOpportunity[]>(`${this.adminUrl}/opportunities`);
  }

  deleteOpportunity(opportunityId: number): Observable<void> {
    return this.http.delete<void>(`${this.adminUrl}/opportunities/${opportunityId}`);
  }

  listDonations(page = 0, size = 20): Observable<PageResponse<Donation>> {
    const params = new HttpParams().set('page', page).set('size', size);
    return this.http.get<PageResponse<Donation>>(`${this.adminUrl}/donations`, { params });
  }
}
