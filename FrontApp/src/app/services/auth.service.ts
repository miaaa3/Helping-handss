import { HttpClient } from '@angular/common/http';
import { Inject,Injectable } from '@angular/core';
import { BehaviorSubject, Observable, Subject, tap } from 'rxjs';
import { Volunteer } from '../models/volunteer';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  
  private baseUrl;

  constructor(private http: HttpClient) {
    this.baseUrl = 'http://localhost:8080/auth';
  
   }

  registerVolunteer(registerRequest: any): Observable<any> {
    return this.http.post(this.baseUrl+ '/register/volunteer', registerRequest);
  }

  registerOrganization(registerRequest: any): Observable<any> {
    return this.http.post(this.baseUrl+ '/register/organization', registerRequest);
  }

  login(authenticationRequest: any): Observable<any> {
    return this.http.post(this.baseUrl+ '/login', authenticationRequest);
  }

}

