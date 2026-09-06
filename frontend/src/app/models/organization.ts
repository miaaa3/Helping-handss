import { User } from "./user";

export interface Organization extends User {
    name: string;
    description: string;
    website: string;
    logo: string;
    type: string;
    founder: string;
    foundedAt: Date;
  }
  