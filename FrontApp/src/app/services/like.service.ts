import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { TokenStorageService, USER_ID } from './token-storage.service';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class LikeService {
  private likeUrl: string;
  userId = sessionStorage.getItem(USER_ID);

  constructor(private http: HttpClient, private tokenStorage: TokenStorageService) {
    this.likeUrl = 'http://localhost:8080/H/likes';
  }

  private getAuthorizationHeader(): HttpHeaders {
    const token = this.tokenStorage.getToken(); 
    return new HttpHeaders({
      'Authorization': `Bearer ${token}`
    });
  }

  createLike(postId: number): Observable<any> {
    const requestOptions = {
      headers: this.getAuthorizationHeader()
    };

    const formData = new FormData();
    formData.append('postId', ''+postId);
    formData.append('userId', sessionStorage.getItem(USER_ID)!);
  
    return this.http.post(`${this.likeUrl}/createLike`, formData, requestOptions);
  }
  

  LikesNumber(postId: number): Observable<any>{
    const requestOptions = {
      headers: this.getAuthorizationHeader()
    };
    
    return this.http.get(`${this.likeUrl}/getLikesNumber/`+postId,requestOptions);

  }
}
