import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';
import { CreateDonationIntentResponse, CreateDonationRequest, Donation } from '../models/donation';

@Injectable({
  providedIn: 'root'
})
export class DonationService {
  private donationUrl: string;

  constructor(private http: HttpClient) {
    this.donationUrl = `${environment.apiUrl}api/donations`;
  }

  createDonationIntent(request: CreateDonationRequest): Observable<CreateDonationIntentResponse> {
    return this.http.post<CreateDonationIntentResponse>(`${this.donationUrl}/create-intent`, request);
  }

  getOrganizationDonations(organizationId: number): Observable<Donation[]> {
    return this.http.get<Donation[]>(`${this.donationUrl}/organization/${organizationId}`);
  }

  getTotalRaised(organizationId: number): Observable<number> {
    return this.http.get<number>(`${this.donationUrl}/organization/${organizationId}/total`);
  }

  getMyDonations(): Observable<Donation[]> {
    return this.http.get<Donation[]>(`${this.donationUrl}/my-donations`);
  }

  getStripeConfig(): Observable<{ publishableKey: string }> {
    return this.http.get<{ publishableKey: string }>(`${this.donationUrl}/config`);
  }
}
