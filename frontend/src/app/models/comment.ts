import { Post } from "./post";
import { User } from "./user";

export interface Comment {
    content: string;
    user: User;
    createdAt:Date
}
