export class Volunteer {
    id?: number;
    firstName?: string;
     lastName?: string;
     email?: string;
     password?: string;
     phone?: string;
     address?: string;
     profilePicture?: string;
     gender?: string;
     interests?: string[];
     socialMediaProfiles?: { [key: string]: string };
     roles?: string[];
     createdAt?: Date;
     birthdate?: Date;
     _token?:string;
     _tokenExpirationDate?: Date;
    

    constructor(){}
}
