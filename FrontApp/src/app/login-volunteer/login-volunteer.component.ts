import { Component, OnInit } from '@angular/core';
import { VolunteerServiceService } from '../services/volunteerservice';
import { AuthenticationRequest } from '../models/authentication-request';
import { Router } from '@angular/router';

@Component({
  selector: 'app-login-volunteer',
  templateUrl: './login-volunteer.component.html',
  styleUrls: ['./login-volunteer.component.css']
})
export class LoginVolunteerComponent {
  authenticationRequest: AuthenticationRequest = {
    email: '',
    password: ''
  };
  toastr: any;

  constructor(private volunteerService : VolunteerServiceService, private router: Router,  private toastr: ToastrService) {}
  login() {
    this.volunteerService.login(this.authenticationRequest).subscribe({
      next: (response) => {
        // Check if you receive a successful response from the backend
        if (response && response.statusCodeValue === 200) {
          // Successful login, navigate to the home page
          this.router.navigate(['/home2']);
        } else {
          // Extract error message from the response body
          const errorMessage = response.body;
  
          // Display the error message to the user
          this.toastr.error(errorMessage, 'Login Failed', {
            timeOut: 3000,
            progressBar: true,
            closeButton: true,
            enableHtml: true
          });
        }
      },
      error: (error) => {
        // Handle other error cases here
        console.error('Unexpected error:', error);
  
        // Display a generic error message to the user
        this.toastr.error('An unexpected error occurred', 'Login Failed', {
          timeOut: 3000,
          progressBar: true,
          closeButton: true,
          enableHtml: true
        });
      }
    });
  }
}