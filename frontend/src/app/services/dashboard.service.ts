import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';
import { OrganizationDashboard, VolunteerDashboard } from '../models/dashboard';

@Injectable({
  providedIn: 'root'
})
export class DashboardService {
  private dashboardUrl: string;

  constructor(private http: HttpClient) {
    this.dashboardUrl = `${environment.apiUrl}api/dashboard`;
  }

  getVolunteerDashboard(): Observable<VolunteerDashboard> {
    return this.http.get<VolunteerDashboard>(`${this.dashboardUrl}/volunteer`);
  }

  getOrganizationDashboard(): Observable<OrganizationDashboard> {
    return this.http.get<OrganizationDashboard>(`${this.dashboardUrl}/organization`);
  }
}
