import { Component, OnInit } from '@angular/core';
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
import { OpportunityService } from '../services/opportunity.service';


@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})

export class HomeComponent implements OnInit {

  selectedFile: File | undefined;
  post: Post = {} as Post; 
  user: Volunteer = {} as Volunteer;
  isToastVisible=false;
  email?: string;
  userId!: number;
  selectedFiles: File[] = []; 
  following: User[]=[];
  suggestions: User[]=[];
  numberOfFollowing!:number;
  numberOfFollowers!: number
  feedOpportunities: Opportunity[] = [];

  ngOnInit(){
    this.getUser();
    this.getFollowing()
    this.getSuggestions()
    this.getFeedOpportunities()
  }

  constructor(private postService : PostService,private userService:UserService,
     private router: Router, private userDataService : UserDataService, private followService: FollowService,
     private opportunityService: OpportunityService) {
    this.userId=Number(sessionStorage.getItem(USER_ID));
  }

  onFileSelected(event: Event): void {
    const inputElement = event.target as HTMLInputElement;
    if (inputElement.files) {
      for (let i = 0; i < inputElement.files.length; i++) {
        this.selectedFiles.push(inputElement.files[i]); // Add each selected file to the array
      }
    }
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

    const formData = new FormData();
    formData.append('content', this.post.content!);

    // Check if file are selected
    if (this.selectedFiles.length > 0) {
      for (let i = 0; i < this.selectedFiles.length; i++) {
        formData.append('files', this.selectedFiles[i]);
      }
    }
    

    this.postService.createPost(this.post.content!, this.selectedFiles).subscribe(
      (response: any) => {
        if (response) {
          console.log('Post created successfully:', response);
          // Clear the form 
          this.post.content = '';
          this.selectedFiles = [];
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

  /** Follow a suggested user and drop them from the "People you may know" list. */
  followSuggestion(userId: number | undefined): void {
    if (!userId) return;
    this.followService.follow(userId).subscribe(
      () => {
        this.suggestions = this.suggestions.filter((u) => u.id !== userId);
        this.numberOfFollowing++;
      },
      (error) => {
        console.error('Error following user:', error);
      }
    );
  }

}
