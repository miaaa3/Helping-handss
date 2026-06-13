import { Component, OnInit } from '@angular/core';
import { UserService } from '../services/user.service';
import { Router } from '@angular/router';
import { TokenStorageService } from '../services/token-storage.service';
import { Volunteer } from '../models/volunteer';

@Component({
  selector: 'app-settings',
  templateUrl: './settings.component.html',
  styleUrls: ['./settings.component.css']
})
export class SettingsComponent implements OnInit {
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
  constructor(private  userService: UserService, private router : Router, private tokenStorage : TokenStorageService){}

  selectedInterests: Set<string> = new Set();

  ngOnInit(): void {
    this.userService.getUser().subscribe({
      next: (response: any) => {
        const user = response?.user as Volunteer;
        if (user) {
          this.volunteer = user;
          (user.interests ?? []).forEach(interest => this.selectedInterests.add(interest));
        }
      },
      error: (error) => {
        console.error('Failed to load profile:', error);
      }
    });
  }

  isSelected(interest: string): boolean {
    return this.selectedInterests.has(interest);

  }

  toggleSelection(interest: string): void {
    if (this.isSelected(interest)) {
      this.selectedInterests.delete(interest);
    } else {
      this.selectedInterests.add(interest);
    }
  }

  onSubmit(): void {
    this.volunteer.interests = Array.from(this.selectedInterests);
    this.userService.updateVolunteer(this.volunteer).subscribe({
      next: (response) => {
        console.log('Profile updated:', response);
        this.router.navigate(['/home']);
      },
      error: (error) => {
        console.error('Profile update failed:', error);
      }
    });
  }

}
