import { Injectable } from '@angular/core';
import { Volunteer } from '../models/volunteer';

export const TOKEN_KEY ='auth-token-volunteer';
const VOLUNTEER_KEY='auth-volunteer';
export const USER_ID='user-id'

@Injectable({
  providedIn: 'root'
})
export class TokenStorageService {

  constructor() { }

  public saveToken(token : string){
    window.sessionStorage.removeItem(TOKEN_KEY);
    window.sessionStorage.setItem(TOKEN_KEY,token);
  }

  public getToken(){
    return sessionStorage.getItem(TOKEN_KEY)
  }

  public saveUserID(id:any){
    window.sessionStorage.removeItem(USER_ID);
    window.sessionStorage.setItem(USER_ID,id)
  }

  public getVolunteerEmail(){
    return window.sessionStorage.getItem(VOLUNTEER_KEY)
  }

  public getVolunteerId(){
    return window.sessionStorage.getItem(USER_ID);
  }
}
