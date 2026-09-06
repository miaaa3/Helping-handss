import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';
import { Application } from '../models/application';

@Injectable({
  providedIn: 'root'
})
export class ApplicationService {
  private applicationsUrl: string;

  constructor(private http: HttpClient) {
    this.applicationsUrl = `${environment.apiUrl}api/applications`;
  }

  /** Volunteer applies to an open opportunity. */
  apply(opportunityId: number): Observable<Application> {
    return this.http.post<Application>(`${this.applicationsUrl}/apply`, null, {
      params: new HttpParams().set('opportunityId', opportunityId)
    });
  }

  /** Volunteer withdraws their own application. */
  withdraw(applicationId: number): Observable<Application> {
    return this.http.delete<Application>(`${this.applicationsUrl}/${applicationId}`);
  }

  /** The current volunteer's own applications - for the volunteer dashboard. */
  getMyApplications(): Observable<Application[]> {
    return this.http.get<Application[]>(`${this.applicationsUrl}/my`);
  }

  /** All applicants for one opportunity - only visible to the owning organization. */
  getApplicants(opportunityId: number): Observable<Application[]> {
    return this.http.get<Application[]>(`${this.applicationsUrl}/opportunity/${opportunityId}`);
  }

  /** Organization accepts or rejects a pending application. */
  decide(applicationId: number, accept: boolean): Observable<Application> {
    return this.http.put<Application>(`${this.applicationsUrl}/${applicationId}/decision`, null, {
      params: new HttpParams().set('accept', accept)
    });
  }
}
