import { Component, OnInit, ViewChild } from '@angular/core';
import { Post } from '../models/post';
import { PostService } from '../services/post.service';
import { Router } from '@angular/router';
import { UserService } from '../services/user.service';
import { User } from '../models/user';
import { UserDataService } from '../services/user-data.service';
import { Volunteer } from '../models/volunteer';
import { USER_ID } from '../services/token-storage.service';
import { FollowService } from '../services/follow.service';
import { Opportunity } from '../models/opportunity';
import { OpportunityService, SuggestedOrg } from '../services/opportunity.service';
import { PostComponent } from '../post/post.component';
import { ToastrService } from 'ngx-toastr';


@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})

export class HomeComponent implements OnInit {

  @ViewChild(PostComponent) postComponent!: PostComponent;

  selectedFile: File | undefined;
  post: Post = {} as Post;
  user: Volunteer = {} as Volunteer;
  isToastVisible=false;
  email?: string;
  userId!: number;
  selectedFiles: File[] = [];
  filePreviews: string[] = [];
  composerExpanded = false;
  following: User[]=[];
  suggestions: User[]=[];
  numberOfFollowing!:number;
  numberOfFollowers!: number
  feedOpportunities: Opportunity[] = [];
  suggestedOrgs: SuggestedOrg[] = [];

  ngOnInit(){
    this.getUser();
    this.getFollowing()
    this.getSuggestions()
    this.getSuggestedOrgs()
    this.getFeedOpportunities()
  }

  constructor(private postService : PostService,private userService:UserService,
     private router: Router, private userDataService : UserDataService, private followService: FollowService,
     private opportunityService: OpportunityService, private toastr: ToastrService) {
    this.userId=Number(sessionStorage.getItem(USER_ID));
  }

  onFileSelected(event: Event): void {
    const inputElement = event.target as HTMLInputElement;
    if (inputElement.files) {
      for (let i = 0; i < inputElement.files.length; i++) {
        const file = inputElement.files[i];
        this.selectedFiles.push(file);
        this.filePreviews.push(URL.createObjectURL(file));
      }
    }
    this.composerExpanded = true;
    inputElement.value = '';
  }

  /** Removes a pending attachment (and its preview) before posting. */
  removeSelectedFile(index: number): void {
    URL.revokeObjectURL(this.filePreviews[index]);
    this.selectedFiles.splice(index, 1);
    this.filePreviews.splice(index, 1);
  }

  /** Collapses the composer and discards any unposted content/attachments. */
  cancelComposer(): void {
    this.post.content = '';
    this.filePreviews.forEach((url) => URL.revokeObjectURL(url));
    this.selectedFiles = [];
    this.filePreviews = [];
    this.composerExpanded = false;
  }

  getUser(){
      this.userService.getUser().subscribe(
        (data: any) => {
          this.user=data.user
          this.numberOfFollowers=data.numberOfFollowers;
          this.numberOfFollowing=data.numberOfFollowing;
          if(this.user.role=='VOLUNTEER')
            this.user = this.user as Volunteer;
          else if(this.user.role=='ORGANIZATION')
          this.user = this.user as Volunteer;
        
        },
        (error) => {
          console.error('Error getting user data:', error);
        }
      );
    }
 
  createPost(): void {
    // Check if content is provided
    if (!this.post.content && this.selectedFiles.length==0) {
      alert('Please enter content for the post.');
      return;
    }

    this.postService.createPost(this.post.content!, this.selectedFiles).subscribe(
      (response: Post) => {
        if (response) {
          // Show the new post at the top of the feed immediately.
          this.postComponent?.prependPost(response, this.user);
          // Clear/collapse the composer
          this.cancelComposer();
        }
      },
      (error: any) => {
        console.error('Post creation error:', error);
      }
    );
  }
  getFollowing() {
    this.followService.getFollowing().subscribe(
      (following) => {
        this.following = following;
      },
      (error) => {
        console.error('Error fetching followers:', error);
      }
    );
  }

  getSuggestedOrgs() {
    this.opportunityService.getSuggestedOrganizations().subscribe({
      next: (orgs) => this.suggestedOrgs = orgs || [],
      error: () => this.suggestedOrgs = []
    });
  }

  /** Follow a suggested organization and drop it from the list. */
  followOrg(orgId: number) {
    this.followService.follow(orgId).subscribe({
      next: (resp) => {
        this.suggestedOrgs = this.suggestedOrgs.filter((o) => o.id !== orgId);
        if (resp.status === 'PENDING') { this.toastr.info('Follow request sent!'); }
        else { this.toastr.success('Following!'); }
      },
      error: () => this.toastr.error('Could not follow right now.')
    });
  }

  getSuggestions() {
    this.userService.getSuggestions().subscribe(
      (suggestions) => {
        this.suggestions = suggestions;
      },
      (error) => {
        console.error('Error fetching suggestions:', error);
      }
    );
  }

  /** Open opportunities from organizations the user follows, for the feed highlight strip. */
  getFeedOpportunities(): void {
    this.opportunityService.getForFeed(this.userId).subscribe(
      (opportunities) => {
        this.feedOpportunities = opportunities.filter((o) => o.status === 'OPEN').slice(0, 3);
      },
      (error) => {
        console.error('Error fetching feed opportunities:', error);
      }
    );
  }

  /** Send a follow request to a suggested user and remove them from the list. */
  followSuggestion(userId: number | undefined): void {
    if (!userId) return;
    this.followService.follow(userId).subscribe({
      next: (resp) => {
        this.suggestions = this.suggestions.filter((u) => u.id !== userId);
        if (resp.status === 'PENDING') {
          this.toastr.info('Follow request sent!');
        }
      },
      error: (err) => console.error('Error sending follow request:', err)
    });
  }

}
