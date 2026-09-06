import { Component, OnInit } from '@angular/core';
import { ToastrService } from 'ngx-toastr';
import { Donation } from '../../models/donation';
import { AdminService } from '../../services/admin.service';

const STATUS_CLASSES: Record<string, string> = {
  PENDING: 'bg-yellow-100 text-yellow-700',
  SUCCEEDED: 'bg-green-light/10 text-green',
  FAILED: 'bg-red-100 text-red-500',
  REFUNDED: 'bg-gray-100 text-gray-500'
};

/** Admin tab: read-only view of platform-wide donation activity. */
@Component({
  selector: 'app-admin-donations',
  templateUrl: './admin-donations.component.html',
  styleUrls: ['./admin-donations.component.css']
})
export class AdminDonationsComponent implements OnInit {
  donations: Donation[] = [];
  loading = true;
  page = 0;
  last = true;

  constructor(private adminService: AdminService, private toastr: ToastrService) {}

  ngOnInit(): void {
    this.load();
  }

  load(page = 0): void {
    this.loading = true;
    this.adminService.listDonations(page).subscribe({
      next: (result) => {
        this.donations = page === 0 ? result.content : [...this.donations, ...result.content];
        this.page = result.number;
        this.last = result.last;
        this.loading = false;
      },
      error: (err) => {
        console.error('Error loading donations:', err);
        this.toastr.error('Could not load donations.');
        this.loading = false;
      }
    });
  }

  loadMore(): void {
    if (!this.last) this.load(this.page + 1);
  }

  statusClass(status: string): string {
    return STATUS_CLASSES[status] ?? 'bg-gray-100 text-gray-500';
  }
}
