import { Component, OnInit } from '@angular/core';
import { AuthenticationRequest } from '../models/authentication-request';
import { Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { AuthenticationResponse } from '../models/authentication-response';
import { TokenStorageService } from '../services/token-storage.service';
import { AuthService } from '../services/auth.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit{
  authenticationRequest: AuthenticationRequest = {} as AuthenticationRequest;
  authenticationResponse: AuthenticationResponse = {} as AuthenticationResponse; 
  isToastVisible = false;
  errorMessage: any; 
  roles:string[]=[]

  constructor(private authService : AuthService, private router: Router,  private toastr: ToastrService, private tokenStorage: TokenStorageService) {}

    ngOnInit(): void {
     
      }
   
  login() {
    this.authService.login(this.authenticationRequest).subscribe({
      next: (response) => {
        if (response && response.statusCodeValue === 200) {
          this.tokenStorage.saveToken(response.body.access_token);
          this.tokenStorage.saveUserID(response.body.id);
          this.router.navigate(['/home']);
        } else {
          this.errorMessage = response.body;
          this.showToast( ); 
        }
      }
    });
  }
  
  reloadPage(){
    window.location.reload;
  }
  
  showToast() {
    this.isToastVisible = true;
    
    setTimeout(() => {
      this.isToastVisible = false;
    }, 5000); 
  }
}  