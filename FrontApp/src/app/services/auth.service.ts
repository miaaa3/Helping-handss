import { HttpClient } from '@angular/common/http';
import { Inject,Injectable } from '@angular/core';
import { BehaviorSubject, Observable, Subject, tap } from 'rxjs';
import { Volunteer } from '../models/volunteer';
import { AuthenticationRequest } from '../models/authentication-request';
import { AuthenticationResponse } from '../models/authentication-response';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  
  private authUrl;

  constructor(private http: HttpClient) {
    this.authUrl = 'http://localhost:8080/auth';
  
   }

  registerVolunteer(registerRequest: any): Observable<any> {
    return this.http.post(this.authUrl+ '/register/volunteer', registerRequest);
  }

  registerOrganization(registerRequest: any): Observable<any> {
    return this.http.post(this.authUrl+ '/register/organization', registerRequest);
  }

    login(authenticationRequest: AuthenticationRequest): Observable<AuthenticationResponse> {
    return this.http.post<AuthenticationResponse>(`${this.authUrl}/login`, authenticationRequest)
  }

}

