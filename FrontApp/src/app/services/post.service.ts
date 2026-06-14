import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Post } from '../models/post';
import { Observable } from 'rxjs';
import { USER_ID } from './token-storage.service';
import { PostDTO } from '../models/postDTO';
import { PageResponse } from '../models/page';
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


  /** Feed of posts from followed users, newest first, one page at a time. */
  getAllPosts(page: number = 0, size: number = 10): Observable<PageResponse<PostDTO>> {
    return this.http.get<PageResponse<PostDTO>>(`${this.postUrl}/getAllPosts?page=${page}&size=${size}`);
  }

  /** All posts by one user, newest first - for profile pages, one page at a time. */
  getPostsByUser(userId: number, page: number = 0, size: number = 10): Observable<PageResponse<PostDTO>> {
    return this.http.get<PageResponse<PostDTO>>(`${this.postUrl}/getPostsByUser?userId=${userId}&page=${page}&size=${size}`);
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
