export interface MessageRequest {
  receiverId: number;
  content: string;
}

export interface MessageResponse {
  id: number;
  senderId: number;
  senderName: string;
  receiverId: number;
  receiverName: string;
  content: string;
  createdAt: string;
  read: boolean;
}

export interface ConversationSummary {
  otherUserId: number;
  otherUserName: string;
  otherUserProfile: string;
  lastMessage: string;
  lastMessageAt: string;
  unreadCount: number;
}
