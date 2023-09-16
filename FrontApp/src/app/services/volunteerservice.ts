import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Volunteer } from '../models/volunteer';
import { BehaviorSubject, Observable, Subject, map } from 'rxjs';
import { AuthenticationRequest } from '../models/authentication-request';
import { AuthenticationResponse } from '../models/authentication-response';
import { JwtHelperService } from '@auth0/angular-jwt';

@Injectable({
  providedIn: 'root'
})
export class VolunteerServiceService {
  private volunteersUrl: string;
  currentVolunteerEmail: string = '';
  volunteer= new Subject<Volunteer>;


  constructor(private http: HttpClient) {
    this.volunteersUrl = 'http://localhost:8080/api/volunteers';
  }

  public register(volunteer: Volunteer) {
    return this.http.post<AuthenticationResponse>(this.volunteersUrl + '/register', volunteer);
  }

  public  login(authenticationRequest: AuthenticationRequest): Observable<AuthenticationResponse> {
    return this.http.post<AuthenticationResponse>(this.volunteersUrl + '/login', authenticationRequest)
  }

  public logout(){
    sessionStorage.removeItem('currentVolunteerEmail');
    this.currentVolunteerEmail='';
  } 
    
  public getVolunteers(): Observable<Volunteer[]> {
    return this.http.get<Volunteer[]>(this.volunteersUrl);
  }

  public getVolunteerByEmail(email : string): Observable<Volunteer> {
    return this.http.get<Volunteer>(this.volunteersUrl+'/volunteerByEmail?email='+email);
  }
  
  public getUserRolesByEmail(email: string): Observable<string[]> {
    return this.http.get<string[]>(this.volunteersUrl+'/roles/'+email);
  }

  public updateVolunteer(volunteer : Volunteer, email:string){
    return this.http.post(this.volunteersUrl + '/updateVolunteer', email);
  }

}
