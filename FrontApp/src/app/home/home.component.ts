import { Component, Input } from '@angular/core';
import { Post } from '../models/post';
import { PostService } from '../services/post.service';
import { Router } from '@angular/router';
import { VolunteerServiceService } from '../services/volunteerservice';
import { TokenStorageService, USER_ID } from '../services/token-storage.service';
import { Volunteer } from '../models/volunteer';



@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})

export class HomeComponent {

  selectedFile: File | undefined;
  post: Post = {} as Post; 
  volunteer: Volunteer = {} as Volunteer;
  isToastVisible=false;
  email?: string;
  id?: number;
  selectedFiles: File[] = []; // Array to hold selected files


  constructor(private postService : PostService,private volunteerService : VolunteerServiceService,private tokenStorage : TokenStorageService, private router: Router) {}

  onFileSelected(event: Event): void {
    const inputElement = event.target as HTMLInputElement;
    if (inputElement.files) {
      for (let i = 0; i < inputElement.files.length; i++) {
        this.selectedFiles.push(inputElement.files[i]); // Add each selected file to the array
      }
    }
  }
  
 
  createPost(): void {
    // Check if content is provided
    if (!this.post.content) {
      alert('Please enter content for the post.');
      return;
    }

    const formData = new FormData();
    formData.append('content', this.post.content);

    // Check if files are selected
    if (this.selectedFiles.length > 0) {
      for (let i = 0; i < this.selectedFiles.length; i++) {
        formData.append('files', this.selectedFiles[i]);
      }
    }

    this.postService.createPost(this.post.content, this.selectedFiles).subscribe(
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

  ngOnInit(){
  }

  getVolunteer(){
    this.email=this.tokenStorage.getVolunteerEmail()??'';
    this.volunteerService.getVolunteerByEmail(this.email).subscribe({
      next: (response) => {
        this.volunteer=response;
      }
    })
  }
  
  showToast() {
    this.isToastVisible = true;
    
    setTimeout(() => {
      this.isToastVisible = false;
    }, 9000); 
  }
}  
