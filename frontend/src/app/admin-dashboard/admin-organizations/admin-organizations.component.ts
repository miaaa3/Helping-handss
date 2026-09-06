import { Component, OnInit } from '@angular/core';
import { ToastrService } from 'ngx-toastr';
import { AdminOrganization } from '../../models/admin';
import { AdminService } from '../../services/admin.service';

const STATUS_CLASSES: Record<string, string> = {
  PENDING: 'bg-yellow-100 text-yellow-700',
  VERIFIED: 'bg-green-light/10 text-green',
  REJECTED: 'bg-red-100 text-red-500'
};

/** Admin tab: review organizations and set their verification status. */
@Component({
  selector: 'app-admin-organizations',
  templateUrl: './admin-organizations.component.html',
  styleUrls: ['./admin-organizations.component.css']
})
export class AdminOrganizationsComponent implements OnInit {
  organizations: AdminOrganization[] = [];
  loading = true;
  updatingId: number | null = null;

  constructor(private adminService: AdminService, private toastr: ToastrService) {}

  ngOnInit(): void {
    this.load();
  }

  load(): void {
    this.loading = true;
    this.adminService.listOrganizations().subscribe({
      next: (organizations) => {
        this.organizations = organizations;
        this.loading = false;
      },
      error: (err) => {
        console.error('Error loading organizations:', err);
        this.toastr.error('Could not load organizations.');
        this.loading = false;
      }
    });
  }

  statusClass(status: string): string {
    return STATUS_CLASSES[status] ?? 'bg-gray-100 text-gray-500';
  }

  setVerification(organization: AdminOrganization, status: 'PENDING' | 'VERIFIED' | 'REJECTED'): void {
    if (organization.verificationStatus === status || this.updatingId) return;

    this.updatingId = organization.id;
    this.adminService.setOrganizationVerification(organization.id, status).subscribe({
      next: (updated) => {
        organization.verificationStatus = updated.verificationStatus;
        this.updatingId = null;
        this.toastr.success('Verification status updated.');
      },
      error: (err) => {
        console.error('Error updating verification status:', err);
        this.toastr.error('Could not update verification status.');
        this.updatingId = null;
      }
    });
  }
}
