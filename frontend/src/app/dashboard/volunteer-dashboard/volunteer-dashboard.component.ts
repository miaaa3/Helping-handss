import { Component, OnInit } from '@angular/core';
import { ToastrService } from 'ngx-toastr';
import { MatDialog } from '@angular/material/dialog';
import { Application, ApplicationStatus } from '../../models/application';
import { VolunteerDashboard } from '../../models/dashboard';
import { ApplicationService } from '../../services/application.service';
import { DashboardService } from '../../services/dashboard.service';
import { ConfirmDialogComponent } from '../../helpers/confirm-dialog/confirm-dialog.component';

const STATUS_CLASSES: Record<ApplicationStatus, string> = {
  PENDING: 'bg-yellow-100 text-yellow-700',
  ACCEPTED: 'bg-green-light/10 text-green',
  REJECTED: 'bg-red-100 text-red-500',
  CANCELLED: 'bg-gray-100 text-gray-500'
};

/** Volunteer dashboard: application counts, impact stats, upcoming opportunities and application history. */
@Component({
  selector: 'app-volunteer-dashboard',
  templateUrl: './volunteer-dashboard.component.html',
  styleUrls: ['./volunteer-dashboard.component.css']
})
export class VolunteerDashboardComponent implements OnInit {
  dashboard: VolunteerDashboard | null = null;
  loading = true;
  error = false;

  constructor(
    private dashboardService: DashboardService,
    private applicationService: ApplicationService,
    private toastr: ToastrService,
    private dialog: MatDialog
  ) {}

  ngOnInit(): void {
    this.load();
  }

  load(): void {
    this.loading = true;
    this.error = false;
    this.dashboardService.getVolunteerDashboard().subscribe({
      next: (dashboard) => {
        this.dashboard = dashboard;
        this.loading = false;
      },
      error: (err) => {
        console.error('Error loading volunteer dashboard:', err);
        this.error = true;
        this.loading = false;
      }
    });
  }

  statusClass(status: ApplicationStatus): string {
    return STATUS_CLASSES[status] ?? 'bg-gray-100 text-gray-500';
  }

  withdraw(application: Application): void {
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
        next: () => {
          this.toastr.success('Application withdrawn.');
          this.load();
        },
        error: (err) => {
          console.error('Error withdrawing application:', err);
          this.toastr.error('Could not withdraw this application.');
        }
      });
    });
  }
}
