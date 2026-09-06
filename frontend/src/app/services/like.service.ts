import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { TokenStorageService, USER_ID } from './token-storage.service';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class LikeService {
  private likeUrl: string;
  userId = sessionStorage.getItem(USER_ID);

  constructor(private http: HttpClient, private tokenStorage: TokenStorageService) {
    this.likeUrl = `${environment.apiUrl}api/likes`;
  }

  createLike(postId: number): Observable<any> {
    const formData = new FormData();
    formData.append('postId', ''+postId);
    return this.http.post(`${this.likeUrl}/createLike`, formData);
  }
  
}
