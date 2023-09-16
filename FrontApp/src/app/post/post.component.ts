import { Component, OnInit } from '@angular/core';
import { PostService } from '../services/post.service';
import { Post } from '../models/post';
import { DatePipe } from '@angular/common';
import { LikeService } from '../services/like.service';
import { HttpStatusCode } from '@angular/common/http';
import { PostDTO } from '../models/postDTO';

@Component({
  selector: 'app-post',
  templateUrl: './post.component.html',
  styleUrls: ['./post.component.css']
})
export class PostComponent implements OnInit{
  public posts:any;
  public likesNumber:any;
  public isLiked:any;

  constructor(private postService: PostService, private likeService: LikeService) {}
  ngOnInit(): void {
    this.getAllPosts()
  }
 

  getAllPosts() {
    this.postService.getAllPosts().subscribe({
      next: (data: PostDTO[]) => {
        this.posts = data.map(item => item.post);
        this.isLiked = data.map(item => item.liked);
        this.postService.constructImageUrls(this.posts);
      },
      error: (err: any) => {
        console.error(err);
      },
    },   
    );
  }
  likePost(postId:number) {
    this.likeService.createLike(postId).subscribe(
      (response) => {
      if (response) {  
        this.isLiked=response.isLiked;
        }
      },
      (error) => {
        console.error('Error liking post:', error);
      },
      ()=>{
        this.getAllPosts();
      }
    );}
  
  LikesNumber(postId:number){
    this.likeService.LikesNumber(postId).subscribe({
      next: (data: any) => {
        this.likesNumber=data;
        console.log(data)
      },
      error: (err: any) => {
        console.error(err);
      },
  });  
}
}