import { Component, EventEmitter, Input, Output } from '@angular/core';
import { ConversationSummary } from '../../models/message';

@Component({
  selector: 'app-conversation-list',
  templateUrl: './conversation-list.component.html',
  styleUrls: ['./conversation-list.component.css']
})
export class ConversationListComponent {
  @Input() conversations: ConversationSummary[] = [];
  @Input() selectedUserId: number | null = null;
  @Output() select = new EventEmitter<ConversationSummary>();

  selectConversation(conversation: ConversationSummary): void {
    this.select.emit(conversation);
  }
}
