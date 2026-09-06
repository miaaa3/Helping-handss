import { Component, Inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { ToastrService } from 'ngx-toastr';
import { Opportunity, OpportunityRequest, OPPORTUNITY_CATEGORIES, OPPORTUNITY_STATUSES } from '../../models/opportunity';
import { OpportunityService } from '../../services/opportunity.service';

export interface OpportunityFormData {
  /** Present when editing an existing opportunity; omitted when creating a new one. */
  opportunity?: Opportunity;
}

/** Create/edit dialog for organizations to manage a volunteering opportunity. */
@Component({
  selector: 'app-opportunity-form',
  templateUrl: './opportunity-form.component.html',
  styleUrls: ['./opportunity-form.component.css']
})
export class OpportunityFormComponent {
  categories = OPPORTUNITY_CATEGORIES;
  statuses = OPPORTUNITY_STATUSES;

  form: OpportunityRequest;
  loading = false;
  errorMessage: string | null = null;

  readonly isEdit: boolean;

  constructor(
    public dialogRef: MatDialogRef<OpportunityFormComponent>,
    @Inject(MAT_DIALOG_DATA) public data: OpportunityFormData,
    private opportunityService: OpportunityService,
    private toastr: ToastrService
  ) {
    const existing = data?.opportunity;
    this.isEdit = !!existing;
    this.form = {
      title: existing?.title ?? '',
      description: existing?.description ?? '',
      category: existing?.category ?? 'COMMUNITY',
      date: existing ? existing.date.substring(0, 16) : '',
      location: existing?.location ?? '',
      neededVolunteers: existing?.neededVolunteers ?? 1,
      status: existing?.status ?? 'DRAFT'
    };
  }

  save(): void {
    if (!this.form.title.trim() || !this.form.description.trim() || !this.form.location.trim() || !this.form.date) {
      this.errorMessage = 'Please fill in all required fields.';
      return;
    }
    if (this.form.neededVolunteers < 1) {
      this.errorMessage = 'Needed volunteers must be at least 1.';
      return;
    }

    this.errorMessage = null;
    this.loading = true;

    // datetime-local gives "yyyy-MM-ddTHH:mm" - pad to full LocalDateTime format for the backend.
    const date = this.form.date.length === 16 ? `${this.form.date}:00` : this.form.date;
    const payload: OpportunityRequest = { ...this.form, date };

    const request$ = this.isEdit
      ? this.opportunityService.update(this.data.opportunity!.id, payload)
      : this.opportunityService.create(payload);

    request$.subscribe({
      next: (opportunity) => {
        this.loading = false;
        this.toastr.success(this.isEdit ? 'Opportunity updated.' : 'Opportunity posted.');
        this.dialogRef.close(opportunity);
      },
      error: (err) => {
        this.loading = false;
        this.errorMessage = err?.error?.message || 'Something went wrong. Please try again.';
      }
    });
  }

  close(): void {
    this.dialogRef.close(null);
  }
}
