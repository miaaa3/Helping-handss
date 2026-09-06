import { Volunteer } from "./volunteer";

export interface AuthenticationResponse {
     statusCodeValue: number;
     access_token: string; 
     refresh_token: string; 
     email : string; 
     statusCode : String; 
     body:any
     }

