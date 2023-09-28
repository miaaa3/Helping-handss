import { Component, Input } from '@angular/core';
import { Volunteer } from '../models/volunteer';
import {  Router } from '@angular/router';
import { TokenStorageService } from '../services/token-storage.service';
import { AuthService } from '../services/auth.service';


@Component({
  selector: 'app-registration-page',
  templateUrl: './registration-page.component.html',
  styleUrls: ['./registration-page.component.css']
})
export class RegistrationPageComponent {
 

  volunteer: Volunteer = {} as Volunteer; // Initialize an empty Volunteer object

  constructor(private authService: AuthService , private router:Router, private tokenStorage : TokenStorageService) { }

  onSubmit(): void {
    this.authService.registerVolunteer(this.volunteer).subscribe(
      response => {
        console.log('Registration successful:', response);
        this.tokenStorage.saveToken(response.body.access_token)
        this.tokenStorage.saveUserID(response.body.id);
        this.router.navigate(['/home'])
      },
      error => {
        console.error('Registration failed:', error);
      }
    );
  }


}

