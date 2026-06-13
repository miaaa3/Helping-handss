import { Component, OnDestroy, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { ConversationSummary } from '../models/message';
import { ChatService } from '../services/chat.service';

@Component({
  selector: 'app-messages',
  templateUrl: './messages.component.html',
  styleUrls: ['./messages.component.css']
})
export class MessagesComponent implements OnInit, OnDestroy {
  conversations: ConversationSummary[] = [];
  selectedConversation: ConversationSummary | null = null;

  private messagesSubscription?: Subscription;

  constructor(
    private chatService: ChatService,
    private route: ActivatedRoute,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.chatService.connect();

    this.messagesSubscription = this.chatService.messages$.subscribe((message) => {
      this.updateConversationPreview(message);
    });

    this.loadConversations(() => this.openConversationFromQueryParams());
  }

  ngOnDestroy(): void {
    this.messagesSubscription?.unsubscribe();
  }

  selectConversation(conversation: ConversationSummary): void {
    this.selectedConversation = conversation;
    conversation.unreadCount = 0;
  }

  private loadConversations(onLoaded?: () => void): void {
    this.chatService.getConversations().subscribe((conversations) => {
      this.conversations = conversations;
      onLoaded?.();
    });
  }

  /**
   * Supports deep-linking from the search bar / a profile's "Message" button:
   * /messages?userId=..&name=..&profile=.. opens (or starts) that conversation.
   */
  private openConversationFromQueryParams(): void {
    const params = this.route.snapshot.queryParamMap;
    const userId = Number(params.get('userId'));

    if (!userId) {
      return;
    }

    const existing = this.conversations.find((c) => c.otherUserId === userId);

    this.selectConversation(
      existing ?? {
        otherUserId: userId,
        otherUserName: params.get('name') ?? '',
        otherUserProfile: params.get('profile') ?? '',
        lastMessage: '',
        lastMessageAt: '',
        unreadCount: 0
      }
    );

    // Drop the query params so a refresh doesn't keep re-selecting this conversation.
    this.router.navigate([], { relativeTo: this.route, queryParams: {}, replaceUrl: true });
  }

  /** Keeps the sidebar preview/unread count in sync with live incoming/outgoing messages. */
  private updateConversationPreview(message: { senderId: number; receiverId: number; content: string; createdAt: string }): void {
    const partnerId = message.senderId;
    const existing = this.conversations.find((c) => c.otherUserId === partnerId || c.otherUserId === message.receiverId);

    if (!existing) {
      this.loadConversations();
      return;
    }

    existing.lastMessage = message.content;
    existing.lastMessageAt = message.createdAt;

    const isFromOpenConversation = this.selectedConversation?.otherUserId === existing.otherUserId;
    if (!isFromOpenConversation && existing.otherUserId === message.senderId) {
      existing.unreadCount = (existing.unreadCount ?? 0) + 1;
    }
  }
}
