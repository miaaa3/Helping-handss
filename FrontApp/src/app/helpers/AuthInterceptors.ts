import { HTTP_INTERCEPTORS, HttpHandler, HttpInterceptor, HttpRequest } from "@angular/common/http";
import { TokenStorageService } from "../services/token-storage.service";
import { Injectable } from "@angular/core";

const TOKEN_HEADER_KEY='Authorization'

@Injectable()
export class AuthInterceptor implements HttpInterceptor{
    constructor(private token: TokenStorageService){}
     public intercept(request : HttpRequest<any>, next: HttpHandler){
        let authReq = request;
        const token = this.token.getToken();
        if (token!=null) {
            authReq = request.clone({headers: request.headers.set(TOKEN_HEADER_KEY,'Bearer '+token)});     
        }
        return next.handle(authReq);
     }
}
export const AuthInterceptorProviders = [
    {provide : HTTP_INTERCEPTORS, useClass: AuthInterceptor, multi: true}
];
