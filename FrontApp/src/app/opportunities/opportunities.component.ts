import { Component, OnInit } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { ToastrService } from 'ngx-toastr';
import { Application } from '../models/application';
import { Opportunity, OpportunityFilters, OpportunityStatus, OPPORTUNITY_CATEGORIES } from '../models/opportunity';
import { ApplicationService } from '../services/application.service';
import { OpportunityService } from '../services/opportunity.service';
import { UserService } from '../services/user.service';
import { TokenStorageService } from '../services/token-storage.service';
import { OpportunityFormComponent } from './opportunity-form/opportunity-form.component';
import { ConfirmDialogComponent } from '../helpers/confirm-dialog/confirm-dialog.component';

/** Browse/discovery page for volunteering opportunities, with category/status/location filters. */
@Component({
  selector: 'app-opportunities',
  templateUrl: './opportunities.component.html',
  styleUrls: ['./opportunities.component.css']
})
export class OpportunitiesComponent implements OnInit {
  opportunities: Opportunity[] = [];
  loading = true;
  error = false;

  categories = OPPORTUNITY_CATEGORIES;
  // Drafts are never shown on the public browse page.
  statuses: OpportunityStatus[] = ['OPEN', 'FULL', 'CLOSED'];

  filters: OpportunityFilters = {};

  isOrganization = false;
  isVolunteer = false;
  /** When true, shows this organization's own opportunities (including drafts) instead of the public browse list. */
  showMine = false;
  private organizationId: number | null = null;

  /** The current volunteer's applications, keyed by opportunity id - for the Apply/Withdraw buttons. */
  myApplications = new Map<number, Application>();

  constructor(
    private opportunityService: OpportunityService,
    private applicationService: ApplicationService,
    private userService: UserService,
    private tokenStorage: TokenStorageService,
    private dialog: MatDialog,
    private toastr: ToastrService
  ) {}

  ngOnInit(): void {
    this.userService.getUser().subscribe({
      next: (data: any) => {
        this.isOrganization = data?.user?.role === 'ORGANIZATION';
        this.isVolunteer = data?.user?.role === 'VOLUNTEER';
        this.organizationId = Number(this.tokenStorage.getVolunteerId());
        if (this.isVolunteer) {
          this.loadMyApplications();
        }
      },
      error: (err) => console.error('Error getting user data:', err)
    });
    this.search();
  }

  /** Loads the current volunteer's applications so cards can show Apply/Pending/Accepted state. */
  loadMyApplications(): void {
    this.applicationService.getMyApplications().subscribe({
      next: (applications) => {
        this.myApplications.clear();
        for (const application of applications) {
          // Only the latest application per opportunity matters for the button state.
          const existing = this.myApplications.get(application.opportunity.id);
          if (!existing || new Date(application.createdAt) > new Date(existing.createdAt)) {
            this.myApplications.set(application.opportunity.id, application);
          }
        }
      },
      error: (err) => console.error('Error loading your applications:', err)
    });
  }

  /** Applies to an opportunity and updates that card's state in place. */
  applyToOpportunity(opportunity: Opportunity): void {
    this.applicationService.apply(opportunity.id).subscribe({
      next: (application) => {
        this.myApplications.set(opportunity.id, application);
        this.toastr.success('Application submitted.');
      },
      error: (err) => {
        console.error('Error applying to opportunity:', err);
        this.toastr.error(err?.error?.message || 'Could not submit your application.');
      }
    });
  }

  /** Withdraws a pending application and updates that card's state in place. */
  withdrawApplication(application: Application): void {
    const dialogRef = this.dialog.open(ConfirmDialogComponent, {
      width: '380px',
      data: {
        title: 'Withdraw application?',
        message: `Withdraw your application for "${application.opportunity.title}"?`,
        confirmLabel: 'Withdraw',
        destructive: true
      }
    });

    dialogRef.afterClosed().subscribe((confirmed: boolean) => {
      if (!confirmed) return;

      this.applicationService.withdraw(application.id).subscribe({
        next: (updated) => {
          this.myApplications.set(updated.opportunity.id, updated);
          this.toastr.success('Application withdrawn.');
        },
        error: (err) => {
          console.error('Error withdrawing application:', err);
          this.toastr.error('Could not withdraw this application.');
        }
      });
    });
  }

  /** Returns this volunteer's application for a given opportunity, if any. */
  applicationFor(opportunity: Opportunity): Application | null {
    return this.myApplications.get(opportunity.id) ?? null;
  }

  search(): void {
    this.loading = true;
    this.error = false;
    this.showMine = false;
    this.opportunityService.search(this.filters).subscribe({
      next: (opportunities) => {
        this.opportunities = opportunities;
        this.loading = false;
      },
      error: (err) => {
        console.error('Error loading opportunities:', err);
        this.error = true;
        this.loading = false;
      }
    });
  }

  clearFilters(): void {
    this.filters = {};
    this.search();
  }

  /** Loads this organization's own opportunities, including drafts. */
  loadMine(): void {
    if (!this.organizationId) return;

    this.loading = true;
    this.error = false;
    this.showMine = true;
    this.opportunityService.getByOrganization(this.organizationId).subscribe({
      next: (opportunities) => {
        this.opportunities = opportunities;
        this.loading = false;
      },
      error: (err) => {
        console.error('Error loading your opportunities:', err);
        this.error = true;
        this.loading = false;
      }
    });
  }

  openCreateDialog(): void {
    const dialogRef = this.dialog.open(OpportunityFormComponent, { data: {} });
    dialogRef.afterClosed().subscribe((result) => {
      if (result) {
        this.loadMine();
      }
    });
  }

  editOpportunity(opportunity: Opportunity): void {
    const dialogRef = this.dialog.open(OpportunityFormComponent, { data: { opportunity } });
    dialogRef.afterClosed().subscribe((result) => {
      if (result) {
        this.showMine ? this.loadMine() : this.search();
      }
    });
  }

  deleteOpportunity(opportunity: Opportunity): void {
    const dialogRef = this.dialog.open(ConfirmDialogComponent, {
      width: '380px',
      data: {
        title: 'Delete opportunity?',
        message: `Delete "${opportunity.title}"? This cannot be undone.`,
        confirmLabel: 'Delete',
        destructive: true
      }
    });

    dialogRef.afterClosed().subscribe((confirmed: boolean) => {
      if (!confirmed) return;

      this.opportunityService.delete(opportunity.id).subscribe({
        next: () => {
          this.opportunities = this.opportunities.filter((o) => o.id !== opportunity.id);
          this.toastr.success('Opportunity deleted.');
        },
        error: (err) => {
          console.error('Error deleting opportunity:', err);
          this.toastr.error('Could not delete this opportunity.');
        }
      });
    });
  }
}
