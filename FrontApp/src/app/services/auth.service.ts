import { HttpClient } from '@angular/common/http';
import { Inject,Injectable } from '@angular/core';
import { BehaviorSubject, Observable, tap } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private roles: string[] = [];

  constructor() {
    // Retrieve roles from sessionStorage when the AuthService is initialized
    const rolesJson = sessionStorage.getItem('userRoles');
    if (rolesJson) {
      this.roles = JSON.parse(rolesJson);
    }
  }

  setRoles(roles: string[]): void {
    this.roles = roles;
    // Store roles in sessionStorage
    sessionStorage.setItem('userRoles', JSON.stringify(roles));
  }

  hasRole(role: string): boolean {
    return this.roles.includes(role);
  }
  }

