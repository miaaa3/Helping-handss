import { Injectable } from '@angular/core';
import { HttpRequest, HttpHandler, HttpEvent, HttpInterceptor } from '@angular/common/http';
import { Observable } from 'rxjs';
import { TokenStorageService } from './token-storage.service';
import { environment } from 'src/environments/environment';

const excludedPaths: string[] = ['auth/register', 'auth/login', '/public']; // Define paths to exclude here

@Injectable()
export class HttpInterceptorService implements HttpInterceptor {

    constructor(private tokenStorage: TokenStorageService) { }

    intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
      const token = this.tokenStorage.getToken();
        const isApiUrl = request.url.startsWith(environment.apiUrl);

        const isExcludedPath = excludedPaths.some(excludedPath => request.url.includes(excludedPath));

        if (token && isApiUrl && !isExcludedPath) {
            request = request.clone({
                setHeaders: { Authorization: `Bearer ${token}` }
            });
        }

        return next.handle(request);
    }
}
