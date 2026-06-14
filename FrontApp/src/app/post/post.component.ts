import { Component, HostListener, OnInit } from '@angular/core';
import { PostService } from '../services/post.service';
import { LikeService } from '../services/like.service';
import { PostDTO } from '../models/postDTO';
import { CommentService } from '../services/comment.service';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { USER_ID } from '../services/token-storage.service';
import { User } from '../models/user';
import { Post } from '../models/post';
import { Comment } from '../models/comment';
import { MatDialog } from '@angular/material/dialog';
import { DonateDialogComponent } from '../donation/donate-dialog/donate-dialog.component';
import { ConfirmDialogComponent } from '../helpers/confirm-dialog/confirm-dialog.component';

@Component({
  selector: 'app-post',
  templateUrl: './post.component.html',
  styleUrls: ['./post.component.css']
})
export class PostComponent implements OnInit {
  posts: Post[] = [];
  users: User[] = [];
  usersComment!:User[];
  likesNumber: number[] = [];
  commentsNumber: number[] = [];
  isLiked: boolean[] = [];
  commentForm: FormGroup;
  content!: string;
  userId!:number;
  showDropdown = false;
  showDropdownNotif = false;
  showAllComments = false;

  /** Tracks per-post UI state (indices line up with `posts`). */
  commentsOpen: boolean[] = [];
  commentLoading: boolean[] = [];

  // Pagination / infinite scroll state for the feed.
  private readonly pageSize = 10;
  private page = 0;
  loading = false;
  hasMore = true;

  constructor(private postService: PostService,private likeService: LikeService,private commentService: CommentService,private formBuilder: FormBuilder,private dialog: MatDialog) {
      this.commentForm = this.formBuilder.group({
        comment: ['', Validators.required],
      });
       this.userId=+sessionStorage.getItem(USER_ID)!
  }

  ngOnInit(): void {
    this.loadPosts(true);
  }

  /** Loads the feed page by page. Pass reset=true to reload from the first page. */
  loadPosts(reset: boolean = false): void {
    if (this.loading || (!reset && !this.hasMore)) return;

    if (reset) {
      this.page = 0;
      this.hasMore = true;
      this.posts = [];
      this.users = [];
      this.isLiked = [];
      this.likesNumber = [];
      this.commentsNumber = [];
      this.commentsOpen = [];
      this.commentLoading = [];
    }

    this.loading = true;
    this.postService.getAllPosts(this.page, this.pageSize).subscribe({
      next: (data) => {
        const newPosts = data.content.map(item => item.post);
        this.postService.constructImageUrls(newPosts);

        this.posts.push(...newPosts);
        this.users.push(...data.content.map(item => item.user));
        this.isLiked.push(...data.content.map(item => item.liked));
        this.likesNumber.push(...data.content.map(item => item.likesNumber));
        this.commentsNumber.push(...data.content.map(item => item.commentsNumber));
        this.commentsOpen.push(...newPosts.map(() => false));
        this.commentLoading.push(...newPosts.map(() => false));

        this.hasMore = !data.last;
        this.page++;
        this.loading = false;
      },
      error: (err: any) => {
        console.error('Error loading feed:', err);
        this.loading = false;
      }
    });
  }

  /** Loads the next page once the user nears the bottom of the page. */
  @HostListener('window:scroll')
  onWindowScroll(): void {
    const threshold = 400;
    const reachedBottom = window.innerHeight + window.scrollY >= document.body.offsetHeight - threshold;
    if (reachedBottom) {
      this.loadPosts();
    }
  }

  likePost(postId: number, index: number) {
    this.likeService.createLike(postId).subscribe({
      next: () => {
        this.isLiked[index] = !this.isLiked[index];
        this.likesNumber[index] += this.isLiked[index] ? 1 : -1;
      },
      error: (error) => {
        console.error('Error liking post:', error);
      }
    });
  }

  deletePost(postId: number, index: number) {
    this.showDropdown = false;
    const dialogRef = this.dialog.open(ConfirmDialogComponent, {
      width: '380px',
      data: {
        title: 'Delete post?',
        message: 'This will permanently remove your post. This action cannot be undone.',
        confirmLabel: 'Delete',
        destructive: true
      }
    });

    dialogRef.afterClosed().subscribe((confirmed: boolean) => {
      if (!confirmed) return;

      this.postService.deletePost(postId).subscribe({
        next: () => {
          this.posts.splice(index, 1);
          this.users.splice(index, 1);
          this.isLiked.splice(index, 1);
          this.likesNumber.splice(index, 1);
          this.commentsNumber.splice(index, 1);
          this.commentsOpen.splice(index, 1);
          this.commentLoading.splice(index, 1);
        },
        error: (error) => {
          console.error('Error deleting post:', error);
        }
      });
    });
  }

  createComment(postId: number, index: number) {
    const content = this.commentForm.get('comment')!.value?.trim();
    if (!content || this.commentLoading[index]) return;

    this.commentLoading[index] = true;
    this.commentService.createComment(content, postId).subscribe({
      next: (comment: Comment) => {
        this.posts[index].comments.push(comment);
        this.commentsNumber[index]++;
        this.commentForm.reset();
        this.commentLoading[index] = false;
      },
      error: (error) => {
        console.error('Error commenting post:', error);
        this.commentLoading[index] = false;
      }
    });
  }

  /** Submits on Enter, but lets Shift+Enter insert a newline in the comment box. */
  onCommentKeydown(event: KeyboardEvent, postId: number, index: number) {
    if (event.key === 'Enter' && !event.shiftKey) {
      event.preventDefault();
      this.createComment(postId, index);
    }
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

  /** Expands/collapses the comment thread under a post, focusing the input when opened. */
  toggleComments(index: number) {
    this.commentsOpen[index] = !this.commentsOpen[index];
  }

  openDonateDialog(organization: User) {
    this.dialog.open(DonateDialogComponent, {
      width: '420px',
      data: {
        organizationId: organization.id,
        organizationName: organization.name
      }
    });
  }

}
