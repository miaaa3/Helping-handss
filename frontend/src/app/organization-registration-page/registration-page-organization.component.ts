import { Component } from '@angular/core';
import { Organization } from '../models/organization';
import { AuthService } from '../services/auth.service';
import { Router } from '@angular/router';
import { TokenStorageService } from '../services/token-storage.service';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';

@Component({
  selector: 'app-registration-page-organization',
  templateUrl: './registration-page-organization.component.html',
  styleUrls: ['./registration-page-organization.component.css']
})
export class RegistrationPageOrganizationComponent {
  organization: Organization = {} as Organization; 
  registrationForm: FormGroup;

  constructor(private authService: AuthService , private router:Router, private tokenStorage : TokenStorageService, private fb: FormBuilder) { 
    this.registrationForm = this.fb.group({
      name: ['', Validators.required],
      founderName: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      type: ['', Validators.required],
      phone: ['', Validators.required],
      foundedAt: ['', Validators.required],
      address: ['', Validators.required],
      password: ['', [Validators.required, Validators.minLength(8)]],
    });
  }

  onSubmit(): void {
  if(this.registrationForm.valid){
    this.authService.registerOrganization(this.organization).subscribe(
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
  }}
}
