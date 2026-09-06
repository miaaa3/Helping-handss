import { Post } from "./post";
import { User } from "./user";

export interface Like {
    id?: number;
    post: Post;
    user: User;
    createdAt?: string | null;
}
