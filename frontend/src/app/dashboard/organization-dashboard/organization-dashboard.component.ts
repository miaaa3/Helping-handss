import { Component, OnInit } from '@angular/core';
import { ToastrService } from 'ngx-toastr';
import { Application, ApplicationStatus } from '../../models/application';
import { OrganizationDashboard } from '../../models/dashboard';
import { Opportunity, OpportunityStatus } from '../../models/opportunity';
import { ApplicationService } from '../../services/application.service';
import { DashboardService } from '../../services/dashboard.service';

const OPPORTUNITY_STATUS_CLASSES: Record<OpportunityStatus, string> = {
  DRAFT: 'bg-gray-100 text-gray-500',
  OPEN: 'bg-green-light/10 text-green',
  FULL: 'bg-yellow-100 text-yellow-700',
  CLOSED: 'bg-gray-100 text-gray-500'
};

const APPLICATION_STATUS_CLASSES: Record<ApplicationStatus, string> = {
  PENDING: 'bg-yellow-100 text-yellow-700',
  ACCEPTED: 'bg-green-light/10 text-green',
  REJECTED: 'bg-red-100 text-red-500',
  CANCELLED: 'bg-gray-100 text-gray-500'
};

/** Organization dashboard: opportunity overview, pending applicants, donations and engagement stats. */
@Component({
  selector: 'app-organization-dashboard',
  templateUrl: './organization-dashboard.component.html',
  styleUrls: ['./organization-dashboard.component.css']
})
export class OrganizationDashboardComponent implements OnInit {
  dashboard: OrganizationDashboard | null = null;
  loading = true;
  error = false;

  /** Opportunity id -> applicants, lazily loaded when the panel is expanded. */
  applicants = new Map<number, Application[]>();
  /** Opportunity id -> whether the applicants panel is expanded. */
  expanded = new Set<number>();
  /** Opportunity id -> whether applicants are currently loading. */
  loadingApplicants = new Set<number>();

  constructor(
    private dashboardService: DashboardService,
    private applicationService: ApplicationService,
    private toastr: ToastrService
  ) {}

  ngOnInit(): void {
    this.load();
  }

  load(): void {
    this.loading = true;
    this.error = false;
    this.dashboardService.getOrganizationDashboard().subscribe({
      next: (dashboard) => {
        this.dashboard = dashboard;
        this.loading = false;
      },
      error: (err) => {
        console.error('Error loading organization dashboard:', err);
        this.error = true;
        this.loading = false;
      }
    });
  }

  opportunityStatusClass(status: OpportunityStatus): string {
    return OPPORTUNITY_STATUS_CLASSES[status] ?? 'bg-gray-100 text-gray-500';
  }

  applicationStatusClass(status: ApplicationStatus): string {
    return APPLICATION_STATUS_CLASSES[status] ?? 'bg-gray-100 text-gray-500';
  }

  isExpanded(opportunity: Opportunity): boolean {
    return this.expanded.has(opportunity.id);
  }

  toggleApplicants(opportunity: Opportunity): void {
    if (this.expanded.has(opportunity.id)) {
      this.expanded.delete(opportunity.id);
      return;
    }
    this.expanded.add(opportunity.id);
    if (!this.applicants.has(opportunity.id)) {
      this.loadingApplicants.add(opportunity.id);
      this.applicationService.getApplicants(opportunity.id).subscribe({
        next: (applications) => {
          this.applicants.set(opportunity.id, applications);
          this.loadingApplicants.delete(opportunity.id);
        },
        error: (err) => {
          console.error('Error loading applicants:', err);
          this.toastr.error('Could not load applicants for this opportunity.');
          this.loadingApplicants.delete(opportunity.id);
          this.expanded.delete(opportunity.id);
        }
      });
    }
  }

  getApplicants(opportunity: Opportunity): Application[] {
    return this.applicants.get(opportunity.id) ?? [];
  }

  isLoadingApplicants(opportunity: Opportunity): boolean {
    return this.loadingApplicants.has(opportunity.id);
  }

  decide(opportunity: Opportunity, application: Application, accept: boolean): void {
    this.applicationService.decide(application.id, accept).subscribe({
      next: (updated) => {
        const list = this.applicants.get(opportunity.id);
        if (list) {
          const index = list.findIndex(a => a.id === updated.id);
          if (index !== -1) {
            list[index] = updated;
          }
        }
        this.toastr.success(`Application ${accept ? 'accepted' : 'rejected'}.`);
        this.dashboardService.getOrganizationDashboard().subscribe({
          next: (dashboard) => this.dashboard = dashboard,
          error: () => {}
        });
      },
      error: (err) => {
        console.error('Error updating application:', err);
        this.toastr.error('Could not update this application.');
      }
    });
  }
}
