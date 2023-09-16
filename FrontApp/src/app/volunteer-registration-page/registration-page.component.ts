import { Component, Input } from '@angular/core';
import { Volunteer } from '../models/volunteer';
import { VolunteerServiceService } from '../services/volunteerservice';
import { Route, Router } from '@angular/router';
import { TokenStorageService } from '../services/token-storage.service';


@Component({
  selector: 'app-registration-page',
  templateUrl: './registration-page.component.html',
  styleUrls: ['./registration-page.component.css']
})
export class RegistrationPageComponent {
 

  volunteer: Volunteer = {} as Volunteer; // Initialize an empty Volunteer object

  constructor(private volunteerService: VolunteerServiceService, private router:Router, private tokenStorage : TokenStorageService) { }

  onSubmit(): void {
    this.volunteerService.register(this.volunteer).subscribe(
      response => {
        console.log('Registration successful:', response);
        this.tokenStorage.saveToken(response.body.access_token)
        this.tokenStorage.saveVolunteerID(response.body.id);
        this.router.navigate(['/home'])
      },
      error => {
        console.error('Registration failed:', error);
      }
    );
  }


}

