import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Client, IMessage } from '@stomp/stompjs';
import SockJS from 'sockjs-client';
import { Observable, Subject } from 'rxjs';
import { environment } from 'src/environments/environment';
import { ConversationSummary, MessageRequest, MessageResponse } from '../models/message';
import { TokenStorageService } from './token-storage.service';

@Injectable({
  providedIn: 'root'
})
export class ChatService {
  private messagesUrl: string;
  private client?: Client;
  private incomingMessages = new Subject<MessageResponse>();

  constructor(private http: HttpClient, private tokenStorage: TokenStorageService) {
    this.messagesUrl = `${environment.apiUrl}api/messages`;
  }

  getConversations(): Observable<ConversationSummary[]> {
    return this.http.get<ConversationSummary[]>(`${this.messagesUrl}/conversations`);
  }

  getConversation(userId: number): Observable<MessageResponse[]> {
    return this.http.get<MessageResponse[]>(`${this.messagesUrl}/conversation/${userId}`);
  }

  /** Stream of messages delivered in real time over the STOMP connection. */
  get messages$(): Observable<MessageResponse> {
    return this.incomingMessages.asObservable();
  }

  /** Opens (or reuses) the authenticated STOMP/WebSocket connection. */
  connect(): void {
    if (this.client?.active) {
      return;
    }

    const token = this.tokenStorage.getToken();

    this.client = new Client({
      webSocketFactory: () => new SockJS(`${environment.apiUrl}ws`),
      connectHeaders: {
        Authorization: `Bearer ${token}`
      },
      reconnectDelay: 5000,
      onConnect: () => {
        this.client?.subscribe('/user/queue/messages', (message: IMessage) => {
          this.incomingMessages.next(JSON.parse(message.body) as MessageResponse);
        });
      }
    });

    this.client.activate();
  }

  disconnect(): void {
    this.client?.deactivate();
    this.client = undefined;
  }

  /** Sends a chat message over the active STOMP connection. */
  sendMessage(request: MessageRequest): void {
    this.client?.publish({
      destination: '/app/chat.send',
      body: JSON.stringify(request)
    });
  }
}
