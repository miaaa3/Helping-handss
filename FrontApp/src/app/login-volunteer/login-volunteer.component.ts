import { Component, OnInit } from '@angular/core';
import { Volunteer } from '../models/volunteer';
import { VolunteerServiceService } from '../services/volunteerservice';
import { AuthenticationRequest } from '../models/authentication-request';
import { AuthenticationResponse } from '../models/authentication-response';

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

  constructor(private volunteerService : VolunteerServiceService) {}

  login() {
    this.volunteerService.login(this.authenticationRequest).subscribe(
      response => this.handle(response));

      }
    handle(response: AuthenticationResponse){
      console.log(response);
    }
}
