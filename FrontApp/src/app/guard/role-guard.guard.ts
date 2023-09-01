import { CanActivateFn, Router } from '@angular/router';
import { VolunteerServiceService } from '../services/volunteerservice';
import { inject } from '@angular/core';
import { AuthService } from '../services/auth.service';

export const roleGuardGuard= (role:  'ADMIN'): CanActivateFn => {
  const guard: CanActivateFn = () => {
    const authService = inject(AuthService);
    const router = inject(Router);

    const hasAccess = authService.hasRole(role);
    return hasAccess ? true : router.createUrlTree(['/welcome-page']);
  };

  return guard;
};