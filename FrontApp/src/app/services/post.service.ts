import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Post } from '../models/post';
import { Observable } from 'rxjs';
import { USER_ID } from './token-storage.service';
import { PostDTO } from '../models/postDTO';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class PostService {
  private postUrl: string;
  private userId: any;

  constructor(private http: HttpClient) {
    this.postUrl = `${environment.apiUrl}api/posts`;
    this.userId = sessionStorage.getItem(USER_ID);
  }


  getAllPosts(): Observable<PostDTO[]> {
    return this.http.get<PostDTO[]>(`${this.postUrl}/getAllPosts`);
  }

  constructImageUrls(posts: Post[]) {
    const baseUrl = `${environment.apiUrl}uploads/`;
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
    if (files) {
      for (let i = 0; i < files.length; i++) {
        formData.append('files', files[i]);
      }
    }

    return this.http.post(`${this.postUrl}/createPost`, formData);
  }

  deletePost(postId:number){
    return this.http.delete(this.postUrl+`/deletePost/`+postId)
  }
}
