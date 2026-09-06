import { Injectable } from '@angular/core';
import { HttpRequest, HttpHandler, HttpEvent, HttpInterceptor, HttpErrorResponse } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { TokenStorageService } from './token-storage.service';
import { environment } from 'src/environments/environment';

const excludedPaths: string[] = ['auth/register', 'auth/login', '/public']; // Define paths to exclude here

@Injectable()
export class HttpInterceptorService implements HttpInterceptor {

    constructor(
      private tokenStorage: TokenStorageService,
      private toastr: ToastrService,
      private router: Router
    ) { }

    intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
      const token = this.tokenStorage.getToken();
        const isApiUrl = request.url.startsWith(environment.apiUrl);

        const isExcludedPath = excludedPaths.some(excludedPath => request.url.includes(excludedPath));

        if (token && isApiUrl && !isExcludedPath) {
            request = request.clone({
                setHeaders: { Authorization: `Bearer ${token}` }
            });
        }

        return next.handle(request).pipe(
          catchError((error: HttpErrorResponse) => this.handleError(error, isExcludedPath))
        );
    }

    /**
     * Surfaces a consistent, user-facing toast for every failed request, so
     * components don't each need their own ad-hoc error handling.
     */
    private handleError(error: HttpErrorResponse, isExcludedPath: boolean): Observable<never> {
      const serverMessage: string | undefined = error.error?.message || error.error?.error;

      switch (error.status) {
        case 0:
          this.toastr.error('Unable to reach the server. Check your connection and try again.', 'Server unavailable');
          break;

        case 401:
          if (isExcludedPath) {
            // Failed login/register attempt - let the message reflect bad credentials.
            this.toastr.error(serverMessage || 'Invalid email or password.', 'Login failed');
          } else if (this.tokenStorage.getToken()) {
            // Only show/redirect once even if several requests 401 at the same time.
            this.toastr.error('Your session has expired. Please log in again.', 'Session expired');
            this.tokenStorage.clear();
            this.router.navigate(['/login']);
          }
          break;

        case 403:
          this.toastr.error("You don't have permission to do that.", 'Access denied');
          break;

        case 400:
        case 422:
          this.toastr.error(serverMessage || 'Please check the information you submitted.', 'Validation error');
          break;

        case 402:
          this.toastr.error(serverMessage || 'Your payment could not be processed.', 'Payment failed');
          break;

        case 500:
        case 502:
        case 503:
        case 504:
          this.toastr.error('Something went wrong on our end. Please try again later.', 'Server error');
          break;

        default:
          if (error.url?.includes('/donations/')) {
            this.toastr.error(serverMessage || 'Your payment could not be processed.', 'Payment failed');
          } else {
            this.toastr.error(serverMessage || 'Something went wrong. Please try again.', 'Error');
          }
      }

      return throwError(() => error);
    }
}
