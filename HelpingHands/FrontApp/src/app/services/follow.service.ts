import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { User } from '../models/user';
import { environment } from 'src/environments/environment';

export interface FollowResponse {
  follow: any;
  followed: boolean;
  status: string | null; // "PENDING" | null
}

export interface PendingRequest {
  id: number;
  follower: User;
  following: User;
  followedAt: string;
  status: string;
}

@Injectable({
  providedIn: 'root'
})
export class FollowService {
  followUrl: string;

  constructor(private http: HttpClient) {
    this.followUrl = `${environment.apiUrl}api/follow`;
  }

  getFollowing(): Observable<User[]> {
    return this.http.get<User[]>(`${this.followUrl}/getFollowing`);
  }

  follow(userId: number): Observable<FollowResponse> {
    const formData = new FormData();
    formData.append('userId', '' + userId);
    return this.http.post<FollowResponse>(`${this.followUrl}/follow`, formData);
  }

  getPendingRequests(): Observable<PendingRequest[]> {
    return this.http.get<PendingRequest[]>(`${this.followUrl}/requests/pending`);
  }

  acceptRequest(followId: number): Observable<any> {
    return this.http.post<any>(`${this.followUrl}/requests/${followId}/accept`, {});
  }

  rejectRequest(followId: number): Observable<any> {
    return this.http.post<any>(`${this.followUrl}/requests/${followId}/reject`, {});
  }
}
