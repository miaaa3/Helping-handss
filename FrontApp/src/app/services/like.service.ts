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
    this.likeUrl = 'http://localhost:8080/api/likes';
  }

  createLike(postId: number): Observable<any> {
    const formData = new FormData();
    formData.append('postId', ''+postId);
    return this.http.post(`${this.likeUrl}/createLike`, formData);
  }
  
}
