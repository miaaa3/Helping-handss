import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import {  USER_ID } from './token-storage.service';
import { Observable } from 'rxjs/internal/Observable';
import { Comment } from '../models/comment';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class CommentService {

  commentUrl:string;

  constructor(private http: HttpClient) {
    this.commentUrl = `${environment.apiUrl}api/comments`;
  }

  createComment(content: string, postId:any): Observable<any> {
    const formData: FormData = new FormData();
    formData.append('content', content);
    formData.append('postId',postId);
    return this.http.post(`${this.commentUrl}/createComment`, formData);
  }

  getCommentsByPostId(postId: number): Observable<Comment[]> {
    return this.http.get<Comment[]>( `${this.commentUrl}/getCommentsByPostId?postId=${postId}`);
  }
}