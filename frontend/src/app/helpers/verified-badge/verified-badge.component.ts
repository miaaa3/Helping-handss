import { Component, Input } from '@angular/core';

/**
 * Small checkmark badge shown next to an organization's name once an
 * admin has verified the account. Renders nothing for any other status.
 */
@Component({
  selector: 'app-verified-badge',
  templateUrl: './verified-badge.component.html',
  styleUrls: ['./verified-badge.component.css']
})
export class VerifiedBadgeComponent {
  @Input() status?: string;

  get isVerified(): boolean {
    return this.status === 'VERIFIED';
  }
}
