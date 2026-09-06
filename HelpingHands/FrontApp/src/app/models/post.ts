import { Comment } from "./comment";
import { Like } from "./like";
import { MediaPost } from "./media-post";
import { User } from "./user";

export interface Post {
  id: number;
  content?: string | null;
  isLiked: boolean;
  media: MediaPost[];
  likes: Like[];
  user: User;
  createdAt?: string | null;
  comments:Comment[];
}