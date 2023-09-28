import { Component, OnInit } from '@angular/core';
import { PostService } from '../services/post.service';
import { LikeService } from '../services/like.service';
import { PostDTO } from '../models/postDTO';
import { CommentService } from '../services/comment.service';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { USER_ID } from '../services/token-storage.service';
import { User } from '../models/user';
import { Post } from '../models/post';
import { Comment } from '../models/comment';

@Component({
  selector: 'app-post',
  templateUrl: './post.component.html',
  styleUrls: ['./post.component.css']
})
export class PostComponent implements OnInit {
  posts!: Post[];
  users!:User[];
  usersComment!:User[];
  likesNumber!: number[];
  commentsNumber!: number[];
  isLiked: any;
  commentForm: FormGroup;
  content!: string;
  userId!:number;
  showDropdown = false;
  showDropdownNotif = false;
  showAllComments = false

  constructor(private postService: PostService,private likeService: LikeService,private commentService: CommentService,private formBuilder: FormBuilder) {
      this.commentForm = this.formBuilder.group({
        comment: ['', Validators.required],
      });
       this.userId=+sessionStorage.getItem(USER_ID)!
  }

  ngOnInit(): void {
    this.getAllPosts();
    
  }

  getAllPosts() {
    this.postService.getAllPosts().subscribe({
      next: (data: PostDTO[]) => {
        this.posts = data.map(item => item.post);   
        console.log(data)
        this.isLiked = data.map(item => item.liked);
        this.users=data.map(item =>item.user); 
        this.likesNumber=data.map(item =>item.likesNumber); 
        this.commentsNumber=data.map(item =>item.commentsNumber); 
        this.postService.constructImageUrls(this.posts); 
      },
      error: (err: any) => {
        console.error(err);
      }
    });
  }

  likePost(postId: number) {
    this.likeService.createLike(postId).subscribe(
      (response) => {
        if (response) {
          this.isLiked = response.isLiked;
        }
      },
      (error) => {
        console.error('Error liking post:', error);
      },
      () => {
        this.getAllPosts();
      }
    );
  }

  deletePost(postId:number){
    this.postService.deletePost(postId).subscribe(
      (response) => {
        if (response) {
          console.log("post deleted")
        }
      },
      (error) => {
        console.error('Error liking post:', error);
      },
      () => {
        this.getAllPosts();
      }
    );
  }

  createComment(postId: number) {
    this.content = this.commentForm.get('comment')!.value;
    this.commentService.createComment(this.content!, postId).subscribe(
      (response) => {
        console.log('comment created successfuly');
        this.commentForm.reset();
      },
      (error) => {
        console.error('Error commenting post:', error);
      },
      () => {
        this.getAllPosts();
      }
    );
  }

  toggleDropdown() {
    this.showDropdown = !this.showDropdown;
  }

  toggleDropdownNotif(){
    this.showDropdownNotif=!this.showDropdownNotif;
  }
  toggleCommentsDisplay() {
    this.showAllComments = !this.showAllComments;
  }

}
