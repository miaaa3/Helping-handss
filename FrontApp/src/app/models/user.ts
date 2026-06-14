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
    bio?: string;
    posts?: Post[]; 
    numberOfFollowing:number;
    numberOfFollowers: number;

    /** Organizations only - moderation status set by admins. */
    verificationStatus?: 'PENDING' | 'VERIFIED' | 'REJECTED';
}
