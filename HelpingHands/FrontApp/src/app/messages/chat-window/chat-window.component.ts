import {
  AfterViewChecked,
  Component,
  ElementRef,
  Input,
  OnChanges,
  OnDestroy,
  OnInit,
  SimpleChanges,
  ViewChild
} from '@angular/core';
import { Subscription } from 'rxjs';
import { ConnectionState, MessageResponse } from '../../models/message';
import { ChatService } from '../../services/chat.service';
import { TokenStorageService } from '../../services/token-storage.service';

@Component({
  selector: 'app-chat-window',
  templateUrl: './chat-window.component.html',
  styleUrls: ['./chat-window.component.css']
})
export class ChatWindowComponent implements OnInit, OnChanges, AfterViewChecked, OnDestroy {
  @Input() otherUserId: number | null = null;
  @Input() otherUserName = '';
  @Input() otherUserProfile = '';

  @ViewChild('scrollContainer') private scrollContainer?: ElementRef<HTMLDivElement>;

  messages: MessageResponse[] = [];
  newMessage = '';
  currentUserId: number | null = null;
  connectionState: ConnectionState = 'disconnected';

  private messagesSubscription?: Subscription;
  private connectionSubscription?: Subscription;
  private shouldScrollToBottom = false;
  private wasDisconnected = false;

  constructor(private chatService: ChatService, private tokenStorage: TokenStorageService) {}

  ngOnInit(): void {
    const id = this.tokenStorage.getVolunteerId();
    this.currentUserId = id ? Number(id) : null;

    this.chatService.connect();

    this.messagesSubscription = this.chatService.messages$.subscribe((message) => {
      if (this.isPartOfConversation(message)) {
        this.addMessage(message);

        // If the other person's message arrives while this conversation is open,
        // tell the backend right away so unread counts stay accurate.
        if (message.senderId === this.otherUserId) {
          this.chatService.markAsRead(this.otherUserId).subscribe();
        }
      }
    });

    this.connectionSubscription = this.chatService.connectionState$.subscribe((state) => {
      // Reconnected after a drop: re-sync this conversation in case messages were missed.
      if (state === 'connected' && this.wasDisconnected && this.otherUserId) {
        this.loadConversation(this.otherUserId);
      }
      this.wasDisconnected = state !== 'connected';
      this.connectionState = state;
    });
  }

  ngOnChanges(changes: SimpleChanges): void {
    if (changes['otherUserId'] && this.otherUserId) {
      this.loadConversation(this.otherUserId);
    }
  }

  ngAfterViewChecked(): void {
    if (this.shouldScrollToBottom) {
      this.scrollToBottom();
      this.shouldScrollToBottom = false;
    }
  }

  ngOnDestroy(): void {
    this.messagesSubscription?.unsubscribe();
    this.connectionSubscription?.unsubscribe();
  }

  send(): void {
    const content = this.newMessage.trim();
    if (!content || !this.otherUserId) {
      return;
    }

    this.chatService.sendMessage({ receiverId: this.otherUserId, content });
    this.newMessage = '';
  }

  isMine(message: MessageResponse): boolean {
    return message.senderId === this.currentUserId;
  }

  private loadConversation(userId: number): void {
    this.messages = [];

    this.chatService.getConversation(userId).subscribe((history) => {
      this.messages = history;
      this.shouldScrollToBottom = true;
    });
  }

  /** Appends an incoming message, skipping it if it's already in the list (REST + STOMP overlap). */
  private addMessage(message: MessageResponse): void {
    if (this.messages.some((m) => m.id === message.id)) {
      return;
    }

    this.messages.push(message);
    this.shouldScrollToBottom = true;
  }

  private isPartOfConversation(message: MessageResponse): boolean {
    if (!this.otherUserId) {
      return false;
    }

    return message.senderId === this.otherUserId || message.receiverId === this.otherUserId;
  }

  private scrollToBottom(): void {
    const container = this.scrollContainer?.nativeElement;
    if (container) {
      container.scrollTop = container.scrollHeight;
    }
  }
}
