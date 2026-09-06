import { Component, OnInit } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { Donation } from '../../models/donation';
import { DonationService } from '../../services/donation.service';
import { DonationReceiptDialogComponent } from '../donation-receipt-dialog/donation-receipt-dialog.component';

@Component({
  selector: 'app-my-donations',
  templateUrl: './my-donations.component.html',
  styleUrls: ['./my-donations.component.css']
})
export class MyDonationsComponent implements OnInit {
  donations: Donation[] = [];
  loading = true;
  errorMessage: string | null = null;

  constructor(
    private donationService: DonationService,
    private dialog: MatDialog
  ) {}

  ngOnInit(): void {
    this.donationService.getMyDonations().subscribe({
      next: (donations) => {
        this.donations = donations;
        this.loading = false;
      },
      error: () => {
        this.errorMessage = 'Could not load your donations. Please try again later.';
        this.loading = false;
      }
    });
  }

  /** Short note shown inline for non-successful donations. */
  statusNote(donation: Donation): string | null {
    switch (donation.status) {
      case 'FAILED':
        return 'Payment failed - you were not charged.';
      case 'REFUNDED':
        return 'This donation was refunded.';
      case 'PENDING':
        return 'Payment is still processing.';
      default:
        return null;
    }
  }

  openReceipt(donation: Donation): void {
    this.dialog.open(DonationReceiptDialogComponent, {
      width: '420px',
      data: { donation }
    });
  }
}
