import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Volunteer } from '../models/volunteer';
import {  Observable, Subject } from 'rxjs';
import { User } from '../models/user';
import { follow } from '../models/follow';
import { SearchResult } from '../models/searchResult';


@Injectable({
  providedIn: 'root'
})
export class UserService {
  private userUrl: string;
  currentVolunteerEmail: string = '';
  volunteer= new Subject<Volunteer>;


  constructor(private http: HttpClient) {
    this.userUrl = 'http://localhost:8080/api/users';
  }

  public getUser(){
    return this.http.get(`${this.userUrl}/getUser`)
  }


  public updateVolunteer(volunteer : Volunteer, email:string){
    return this.http.post<User>(`${this.userUrl}/updateVolunteer`, email);
  }

  searchUsers(keyword: string): Observable<SearchResult[]> {
    return this.http.get<SearchResult[]>(`${this.userUrl}/search?keyword=${keyword}`);
  }


}
