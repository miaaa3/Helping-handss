import { Post } from "./post";

export interface User {
    id?: number;
    email: string;
    password: string;
    profile:string;
    address: string;
    phone: string;
    name: string;
    createdAt?: Date; 
    role: string;
    posts?: Post[]; 
    numberOfFollowing:number;
    numberOfFollowers: number
    
}
