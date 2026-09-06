import { User } from "./user";

export interface Volunteer extends User{
    fullName: string;
    profilePicture: string;
    gender: string;
    birthdate: Date;
    interests: string[];
  }
  