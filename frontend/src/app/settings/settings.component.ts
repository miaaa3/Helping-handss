import { Component, OnInit } from '@angular/core';
import { UserService } from '../services/user.service';
import { Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { TokenStorageService } from '../services/token-storage.service';

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

  volunteer: any = {}; // Holds volunteer or organization profile data
  loading = true;
  saving = false;

  constructor(
    private userService: UserService,
    private router: Router,
    private tokenStorage: TokenStorageService,
    private toastr: ToastrService
  ) {}

  selectedInterests: Set<string> = new Set();

  ngOnInit(): void {
    this.userService.getUser().subscribe({
      next: (response: any) => {
        const user = response?.user;
        if (user) {
          this.volunteer = user;
          (user.interests ?? []).forEach((interest: string) => this.selectedInterests.add(interest));
        }
        this.loading = false;
      },
      error: (error) => {
        console.error('Failed to load profile:', error);
        this.loading = false;
      }
    });
  }

  get isVolunteer(): boolean {
    return this.volunteer.role === 'VOLUNTEER';
  }

  get isOrganization(): boolean {
    return this.volunteer.role === 'ORGANIZATION';
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
    this.saving = true;

    if (this.isOrganization) {
      this.userService.updateOrganization(this.volunteer).subscribe({
        next: () => this.onSaveSuccess(),
        error: (error) => this.onSaveError(error)
      });
      return;
    }

    if (this.isVolunteer) {
      this.volunteer.interests = Array.from(this.selectedInterests);
    }
    this.userService.updateVolunteer(this.volunteer).subscribe({
      next: () => this.onSaveSuccess(),
      error: (error) => this.onSaveError(error)
    });
  }

  private onSaveSuccess(): void {
    this.saving = false;
    this.toastr.success('Profile updated.');
    this.router.navigate(['/user-profile']);
  }

  private onSaveError(error: any): void {
    console.error('Profile update failed:', error);
    this.saving = false;
    this.toastr.error('Could not update profile.');
  }

}
