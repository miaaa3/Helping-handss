import { Injectable } from '@angular/core';
import { CanActivate, Router } from '@angular/router'; // Import CanActivate and Router
import { TokenStorageService } from '../services/token-storage.service';
import { UserDataService } from '../services/user-data.service';

@Injectable({
  providedIn: 'root',
})
export class AuthGuard implements CanActivate {
  constructor(
    private tokenStorageService: TokenStorageService,
    private userDataService: UserDataService,
    private router: Router 
  ) {}

  canActivate(): boolean {
    if (this.tokenStorageService.getToken()) {
        return true;
    }
    this.router.navigate(['/login']);
    return false;
  }
}
