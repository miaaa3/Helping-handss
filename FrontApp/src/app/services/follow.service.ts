import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { follow } from '../models/follow';
import { User } from '../models/user';
import { environment } from 'src/environments/environment';


@Injectable({
  providedIn: 'root'
})
export class FollowService {
  followUrl: string;

  constructor(private http: HttpClient) {
    this.followUrl = `${environment.apiUrl}api/follow`;
  }

  getFollowing() {
    return this.http.get<User[]>(`${this.followUrl}/getFollowing`);
  }

  follow(userId:number){
    const formData = new FormData();
    formData.append('userId', ''+userId);
    return this.http.post<follow>(`${this.followUrl}/follow`, formData);

  }
}
