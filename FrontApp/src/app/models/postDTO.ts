import { Comment } from "./comment";
import { Post } from "./post";
import { User } from "./user";

export interface PostDTO {
    post: Post;
    liked: boolean;
    user: User;
    likesNumber:number;
    commentsNumber: number;
    comments:Comment;
}

