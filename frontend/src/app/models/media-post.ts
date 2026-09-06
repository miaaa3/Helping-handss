import { Post } from "./post";

export interface MediaPost {
    id?: number;
    fileName: string;
    fileType: string;
    file: string; 
    post: Post;
}
