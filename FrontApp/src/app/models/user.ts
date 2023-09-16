import { Post } from "./post";

export interface User {
    id?: number;
    email: string;
    password: string;
    address: string;
    phone: string;
    createdAt?: Date; 
    role: string;
    posts?: Post[]; 
}
