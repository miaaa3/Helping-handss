import { Component, HostListener, OnDestroy, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { ToastrService } from 'ngx-toastr';
import { User } from '../models/user';
import { Post } from '../models/post';
import { PostDTO } from '../models/postDTO';
import { UserService } from '../services/user.service';
import { PostService } from '../services/post.service';
import { LikeService } from '../services/like.service';
import { FollowService } from '../services/follow.service';
import { DonationService } from '../services/donation.service';
import { TokenStorageService } from '../services/token-storage.service';
import { MatDialog } from '@angular/material/dialog';
import { ConfirmDialogComponent } from '../helpers/confirm-dialog/confirm-dialog.component';

/**
 * Profile page - shows the logged-in user's own profile (/user-profile) or
 * another user's profile (/user-profile/:id). Reacts to route param changes
 * so navigating between profiles updates the page without a full reload.
 */
@Component({
  selector: 'app-user-profile',
  templateUrl: './user-profile.component.html',
  styleUrls: ['./user-profile.component.css']
})
export class UserProfileComponent implements OnInit, OnDestroy {
  user: User = {} as User;
  numberOfFollowers = 0;
  numberOfFollowing = 0;
  isFollowing = false;
  isOwnProfile = true;
  followStatus: 'NONE' | 'PENDING' | 'ACCEPTED' = 'NONE';

  loading = true;
  error = false;

  posts: Post[] = [];
  postUsers: User[] = [];
  isLiked: boolean[] = [];
  likesNumber: number[] = [];
  commentsNumber: number[] = [];
  postsLoading = false;

  followBusy = false;
  openDropdownIndex: number | null = null;

  /** Donation progress (organizations only). */
  totalRaised: number | null = null;

  // Pagination / infinite scroll state for the profile's posts.
  private readonly pageSize = 10;
  private page = 0;
  private profileUserId = 0;
  hasMorePosts = true;

  private routeSub?: Subscription;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private userService: UserService,
    private postService: PostService,
    private likeService: LikeService,
    private followService: FollowService,
    private donationService: DonationService,
    private tokenStorage: TokenStorageService,
    private toastr: ToastrService,
    private dialog: MatDialog
  ) {}

  ngOnInit(): void {
    this.routeSub = this.route.paramMap.subscribe((params) => {
      const idParam = params.get('id');
      const ownId = Number(this.tokenStorage.getVolunteerId());
      const profileId = idParam ? Number(idParam) : ownId;
      this.loadProfile(profileId, profileId === ownId);
    });
  }

  ngOnDestroy(): void {
    this.routeSub?.unsubscribe();
  }

  private loadProfile(userId: number, isOwn: boolean): void {
    this.loading = true;
    this.error = false;

    const request$ = isOwn ? this.userService.getUser() : this.userService.getUserById(userId);

    request$.subscribe({
      next: (data: any) => {
        this.user = data.user;
        this.numberOfFollowers = data.numberOfFollowers;
        this.numberOfFollowing = data.numberOfFollowing;
        this.isOwnProfile = isOwn ? true : !!data.ownProfile;
        this.isFollowing = isOwn ? false : !!data.following;
        this.followStatus = isOwn ? 'NONE' : (data.followStatus || 'NONE') as any;
        this.loading = false;

        if (this.isOrganization && this.user.id) {
          this.loadTotalRaised(this.user.id);
        }
      },
      error: (err) => {
        console.error('Error loading profile:', err);
        this.error = true;
        this.loading = false;
      }
    });

    this.loadPosts(userId);
  }

  /** Loads this profile's posts page by page. Pass reset=true to reload from the first page. */
  private loadPosts(userId: number, reset: boolean = true): void {
    if (this.postsLoading || (!reset && !this.hasMorePosts)) return;

    if (reset) {
      this.profileUserId = userId;
      this.page = 0;
      this.hasMorePosts = true;
      this.posts = [];
      this.postUsers = [];
      this.isLiked = [];
      this.likesNumber = [];
      this.commentsNumber = [];
    }

    this.postsLoading = true;
    this.postService.getPostsByUser(userId, this.page, this.pageSize).subscribe({
      next: (data) => {
        const newPosts = data.content.map((item) => item.post);
        this.postService.constructImageUrls(newPosts);

        this.posts.push(...newPosts);
        this.postUsers.push(...data.content.map((item) => item.user));
        this.isLiked.push(...data.content.map((item) => item.liked));
        this.likesNumber.push(...data.content.map((item) => item.likesNumber));
        this.commentsNumber.push(...data.content.map((item) => item.commentsNumber));

        this.hasMorePosts = !data.last;
        this.page++;
        this.postsLoading = false;
      },
      error: (err) => {
        console.error('Error loading posts:', err);
        this.postsLoading = false;
      }
    });
  }

  /** Loads the next page of posts once the user nears the bottom of the page. */
  @HostListener('window:scroll')
  onWindowScroll(): void {
    const threshold = 400;
    const reachedBottom = window.innerHeight + window.scrollY >= document.body.offsetHeight - threshold;
    if (reachedBottom) {
      this.loadPosts(this.profileUserId, false);
    }
  }

  /** Toggles follow/pending/unfollow and updates state in place. */
  toggleFollow(): void {
    if (!this.user.id || this.followBusy) return;

    this.followBusy = true;
    this.followService.follow(this.user.id).subscribe({
      next: (response) => {
        const wasFollowing = this.followStatus === 'ACCEPTED';
        if (response.status === 'PENDING') {
          this.followStatus = 'PENDING';
          this.isFollowing = false;
          this.toastr.info('Follow request sent!');
        } else {
          // cancelled request or unfollowed
          this.followStatus = 'NONE';
          this.isFollowing = false;
          if (wasFollowing) this.numberOfFollowers--;
        }
        this.followBusy = false;
      },
      error: (err) => {
        console.error('Error updating follow status:', err);
        this.toastr.error('Could not update follow status.');
        this.followBusy = false;
      }
    });
  }

  get followButtonLabel(): string {
    if (this.followStatus === 'ACCEPTED') return 'Following';
    if (this.followStatus === 'PENDING') return 'Pending';
    return 'Follow';
  }

  get followButtonClass(): string {
    if (this.followStatus === 'ACCEPTED') return 'bg-gray-100 text-gray-700 hover:bg-gray-200';
    if (this.followStatus === 'PENDING') return 'bg-yellow-100 text-yellow-700 hover:bg-yellow-200';
    return 'bg-green text-white hover:bg-green-dark';
  }

  messageUser(): void {
    this.router.navigate(['/messages'], {
      queryParams: { userId: this.user.id, name: this.user.name, profile: this.user.profile }
    });
  }

  likePost(postId: number, index: number): void {
    this.likeService.createLike(postId).subscribe({
      next: () => {
        this.isLiked[index] = !this.isLiked[index];
        this.likesNumber[index] += this.isLiked[index] ? 1 : -1;
      },
      error: (err) => console.error('Error liking post:', err)
    });
  }

  toggleDropdown(index: number): void {
    this.openDropdownIndex = this.openDropdownIndex === index ? null : index;
  }

  deletePost(postId: number, index: number): void {
    this.openDropdownIndex = null;
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
          this.toastr.success('Post deleted.');
          this.posts.splice(index, 1);
          this.postUsers.splice(index, 1);
          this.isLiked.splice(index, 1);
          this.likesNumber.splice(index, 1);
          this.commentsNumber.splice(index, 1);
        },
        error: (err) => {
          console.error('Error deleting post:', err);
          this.toastr.error('Could not delete post.');
        }
      });
    });
  }

  get isVolunteer(): boolean {
    return this.user.role === 'VOLUNTEER';
  }

  get isOrganization(): boolean {
    return this.user.role === 'ORGANIZATION';
  }

  /** Campaign goal set by the organization (undefined/0 = no goal, hides the progress bar). */
  get campaignGoal(): number {
    return Number((this.user as any).campaignGoal) || 0;
  }

  get donationProgressPercent(): number {
    if (!this.campaignGoal || !this.totalRaised) return 0;
    return Math.min(100, (this.totalRaised / this.campaignGoal) * 100);
  }

  private loadTotalRaised(organizationId: number): void {
    this.donationService.getTotalRaised(organizationId).subscribe({
      next: (total) => (this.totalRaised = total ?? 0),
      error: (err) => console.error('Error loading total raised:', err)
    });
  }
}
