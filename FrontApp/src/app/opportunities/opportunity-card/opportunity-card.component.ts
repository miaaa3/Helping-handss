import { Component, EventEmitter, Input, Output } from '@angular/core';
import { Application } from '../../models/application';
import { Opportunity, OpportunityCategory, OpportunityStatus } from '../../models/opportunity';

const CATEGORY_LABELS: Record<OpportunityCategory, string> = {
  ENVIRONMENT: 'Environment',
  EDUCATION: 'Education',
  HEALTH: 'Health',
  ANIMALS: 'Animals',
  COMMUNITY: 'Community',
  DISASTER_RELIEF: 'Disaster Relief',
  OTHER: 'Other'
};

const STATUS_CLASSES: Record<OpportunityStatus, string> = {
  DRAFT: 'bg-gray-100 text-gray-500',
  OPEN: 'bg-green-light/10 text-green',
  FULL: 'bg-yellow-100 text-yellow-700',
  CLOSED: 'bg-gray-200 text-gray-500'
};

/** Reusable opportunity card - used in the browse page and the home feed. */
@Component({
  selector: 'app-opportunity-card',
  templateUrl: './opportunity-card.component.html',
  styleUrls: ['./opportunity-card.component.css']
})
export class OpportunityCardComponent {
  @Input() opportunity!: Opportunity;
  /** Shows edit/delete actions for the organization that owns this opportunity. */
  @Input() canManage = false;
  /** Shows apply/withdraw actions for volunteers. */
  @Input() isVolunteer = false;
  /** The current volunteer's application to this opportunity, if any. */
  @Input() myApplication: Application | null = null;

  @Output() edit = new EventEmitter<Opportunity>();
  @Output() delete = new EventEmitter<Opportunity>();
  @Output() apply = new EventEmitter<Opportunity>();
  @Output() withdraw = new EventEmitter<Application>();

  get categoryLabel(): string {
    return CATEGORY_LABELS[this.opportunity.category] ?? this.opportunity.category;
  }

  get statusClass(): string {
    return STATUS_CLASSES[this.opportunity.status] ?? 'bg-gray-100 text-gray-500';
  }

  get spotsLabel(): string {
    const remaining = this.opportunity.neededVolunteers - this.opportunity.applicantCount;
    return remaining > 0 ? `${remaining} spot${remaining === 1 ? '' : 's'} left` : 'Fully booked';
  }
}
