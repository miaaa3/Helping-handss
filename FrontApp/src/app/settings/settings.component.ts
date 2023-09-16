import { Component } from '@angular/core';
import { VolunteerServiceService } from '../services/volunteerservice';
import { Router } from '@angular/router';
import { TokenStorageService } from '../services/token-storage.service';
import { Volunteer } from '../models/volunteer';

@Component({
  selector: 'app-settings',
  templateUrl: './settings.component.html',
  styleUrls: ['./settings.component.css']
})
export class SettingsComponent {
  interests: string[] = [
    "Environmental Conservation",
    "Animal Welfare",
    "Community Service",
    "Health and Wellness",
    "Education",
    "Arts and Culture",
    "Social Justice",
    "Elderly Care",
    "Disaster Relief",
    "Technology and Coding",
    "Sports and Recreation",
  ];

  volunteer: Volunteer = {} as Volunteer; // Initialize an empty Volunteer object
  constructor(private volunteerService : VolunteerServiceService, private router : Router, private tokenStorage : TokenStorageService){}

  selectedInterests: Set<string> = new Set();

  isSelected(interest: string): boolean {
    return this.selectedInterests.has(interest);

  }

  toggleSelection(interest: string): void {
    if (this.isSelected(interest)) {
      this.selectedInterests.delete(interest);
      console.log(this.tokenStorage.getToken);
    } else {
      this.selectedInterests.add(interest);
    }
  }
  email = this.tokenStorage.getVolunteerEmail()?.toString() ?? '' ;

  onSubmit(): void {
    this.volunteerService.updateVolunteer(this.volunteer,this.email).subscribe(
      response => {
        console.log('Registration successful:', response);
       
        this.router.navigate(['/home2'])
      },
      error => {
        console.error('Registration failed:', error);
      }
    );
  }
  
}

