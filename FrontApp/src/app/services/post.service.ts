import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Post } from '../models/post';
import { Observable } from 'rxjs';
import { TOKEN_KEY, TokenStorageService, USER_ID } from './token-storage.service';
import { PostDTO } from '../models/postDTO';

@Injectable({
  providedIn: 'root'
})
export class PostService {
  private postUrl: string;
  private UserId?: string;

  constructor(private http: HttpClient, private tokenStorage: TokenStorageService) {
    this.postUrl = 'http://localhost:8080/H/posts';
  }


  private getAuthorizationHeader(): HttpHeaders {
    const token = this.tokenStorage.getToken(); 
    return new HttpHeaders({
      'Authorization': `Bearer ${token}`
    });
  }

  getAllPosts(): Observable<PostDTO[]> {
    const requestOptions = {
      headers: this.getAuthorizationHeader()
    };
    return this.http.get<PostDTO[]>(`${this.postUrl}/getAllPosts`,requestOptions);
  }

  constructImageUrls(posts: Post[]) {
    const baseUrl = 'http://localhost:8081/';
    posts.forEach((post) => {
      post.media.forEach((media) => {
        media.file = baseUrl + media.fileName;
      });
    });
    return posts;
  }
  

  createPost(content: string, files: File[] | null = null): Observable<any> {
    const formData: FormData = new FormData();
    formData.append('content', content);
    const userId = sessionStorage.getItem(USER_ID);

    if (userId) {
      formData.append('userId', userId);
    }
    if (files) {
      for (let i = 0; i < files.length; i++) {
        formData.append('files', files[i]);
      }
    }

    const requestOptions = {
      headers: this.getAuthorizationHeader()
    };

    return this.http.post(`${this.postUrl}/createPost`, formData, requestOptions);
  }
}
