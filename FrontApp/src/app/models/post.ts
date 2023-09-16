import { Like } from "./like";
import { MediaPost } from "./media-post";
import { User } from "./user";

export interface Post {
  id?: number;
  content?: string | null;
  isLiked: boolean;
  media: MediaPost[];
  likes: Like[];
  userEntity: User;
  createdAt?: string | null;
}