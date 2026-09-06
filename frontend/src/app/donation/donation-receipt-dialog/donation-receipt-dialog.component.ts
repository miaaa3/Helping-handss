import { Component, Inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { Donation } from '../../models/donation';

export interface DonationReceiptDialogData {
  donation: Donation;
}

/**
 * Read-only donation receipt - shows organization, date, amount, status,
 * transaction reference and message. Includes a print action so donors
 * can keep a copy for their records.
 */
@Component({
  selector: 'app-donation-receipt-dialog',
  templateUrl: './donation-receipt-dialog.component.html',
  styleUrls: ['./donation-receipt-dialog.component.css']
})
export class DonationReceiptDialogComponent {
  donation: Donation;

  constructor(
    public dialogRef: MatDialogRef<DonationReceiptDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: DonationReceiptDialogData
  ) {
    this.donation = data.donation;
  }

  /** Plain-language explanation of what a status means, shown on failed/refunded receipts. */
  get statusExplanation(): string | null {
    switch (this.donation.status) {
      case 'FAILED':
        return 'This payment did not go through and no funds were transferred. You have not been charged.';
      case 'REFUNDED':
        return 'This donation was refunded. The amount has been returned to your original payment method.';
      case 'PENDING':
        return 'This payment is still being processed by Stripe.';
      default:
        return null;
    }
  }

  close(): void {
    this.dialogRef.close();
  }

  print(): void {
    window.print();
  }
}
