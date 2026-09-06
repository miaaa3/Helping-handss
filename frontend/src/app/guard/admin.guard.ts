import { Injectable } from '@angular/core';
import { CanActivate, Router } from '@angular/router';
import { Observable, catchError, map, of } from 'rxjs';
import { User } from '../models/user';
import { TokenStorageService } from '../services/token-storage.service';
import { UserDataService } from '../services/user-data.service';
import { UserService } from '../services/user.service';

/** Restricts /admin routes to accounts with the ADMIN role. Falls back to a getUser() call if the cache is empty (e.g. on page refresh). */
@Injectable({
  providedIn: 'root',
})
export class AdminGuard implements CanActivate {
  constructor(
    private tokenStorageService: TokenStorageService,
    private userDataService: UserDataService,
    private userService: UserService,
    private router: Router
  ) {}

  canActivate(): Observable<boolean> | boolean {
    if (!this.tokenStorageService.getToken()) {
      this.router.navigate(['/login']);
      return false;
    }

    const cachedUser = this.userDataService.getUser();
    if (cachedUser) {
      return this.checkRole(cachedUser);
    }

    return this.userService.getUser().pipe(
      map((data: any) => {
        const user = data.user;
        this.userDataService.setUser(user);
        return this.checkRole(user);
      }),
      catchError(() => {
        this.router.navigate(['/login']);
        return of(false);
      })
    );
  }

  private checkRole(user: User): boolean {
    if (user.role === 'ADMIN') {
      return true;
    }
    this.router.navigate(['/home']);
    return false;
  }
}
