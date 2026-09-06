import { User } from "./user";

export interface UserDTO {
    user: User;
    numberOfFollowing:number;
    numberOfFollowers: number
}
