import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Volunteer } from '../models/volunteer';
import { Observable } from 'rxjs';
import { AuthenticationRequest } from '../models/authentication-request';
import { AuthenticationResponse } from '../models/authentication-response';

@Injectable({
  providedIn: 'root'
})
export class VolunteerServiceService {
  private volunteersUrl: string;

  constructor(private http: HttpClient) {
    this.volunteersUrl = 'http://localhost:8080/api/volunteers';
  }
  public register(volunteer: Volunteer) {
    return this.http.post<Volunteer>(this.volunteersUrl + '/register', volunteer);
  }

  public  login(authenticationRequest: AuthenticationRequest): Observable<AuthenticationResponse> {
    return this.http.post<AuthenticationResponse>(this.volunteersUrl + '/login', authenticationRequest);
  }
  
  // Method to fetch list of volunteers
  getVolunteers(): Observable<Volunteer[]> {
    return this.http.get<Volunteer[]>(this.volunteersUrl);
  }
  

}
