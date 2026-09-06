import { Component, ElementRef, Inject, ViewChild } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { firstValueFrom } from 'rxjs';
import { loadStripe, Stripe, StripeElements } from '@stripe/stripe-js';
import { ToastrService } from 'ngx-toastr';
import { DonationService } from '../../services/donation.service';

export interface DonateDialogData {
  organizationId: number;
  organizationName: string;
}

type DonateStep = 'amount' | 'payment' | 'success';

@Component({
  selector: 'app-donate-dialog',
  templateUrl: './donate-dialog.component.html',
  styleUrls: ['./donate-dialog.component.css']
})
export class DonateDialogComponent {
  @ViewChild('paymentElement') paymentElementRef!: ElementRef<HTMLDivElement>;

  amount = 10;
  message = '';
  anonymous = false;

  step: DonateStep = 'amount';
  loading = false;
  errorMessage: string | null = null;

  private stripe: Stripe | null = null;
  private elements: StripeElements | null = null;

  constructor(
    public dialogRef: MatDialogRef<DonateDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: DonateDialogData,
    private donationService: DonationService,
    private toastr: ToastrService
  ) {}

  async proceedToPayment(): Promise<void> {
    if (!this.amount || this.amount < 1) {
      this.errorMessage = 'Please enter an amount of at least $1.';
      return;
    }

    this.errorMessage = null;
    this.loading = true;

    try {
      const config = await firstValueFrom(this.donationService.getStripeConfig());
      this.stripe = await loadStripe(config.publishableKey);
      if (!this.stripe) {
        throw new Error('Could not load the payment provider.');
      }

      const intent = await firstValueFrom(this.donationService.createDonationIntent({
        organizationId: this.data.organizationId,
        amount: this.amount,
        currency: 'usd',
        message: this.message,
        anonymous: this.anonymous
      }));

      this.elements = this.stripe.elements({ clientSecret: intent.clientSecret });
      this.step = 'payment';
      this.loading = false;

      setTimeout(() => {
        const paymentElement = this.elements!.create('payment');
        paymentElement.mount(this.paymentElementRef.nativeElement);
      });
    } catch (err: any) {
      this.loading = false;
      this.errorMessage = err?.error?.message || err?.message || 'Something went wrong. Please try again.';
    }
  }

  async confirmPayment(): Promise<void> {
    if (!this.stripe || !this.elements) {
      return;
    }

    this.loading = true;
    this.errorMessage = null;

    const { error } = await this.stripe.confirmPayment({
      elements: this.elements,
      confirmParams: {
        return_url: window.location.href
      },
      redirect: 'if_required'
    });

    this.loading = false;

    if (error) {
      this.errorMessage = error.message || 'Payment failed. Please try again.';
      return;
    }

    this.step = 'success';
    this.toastr.success('Thank you for your donation!');
  }

  close(): void {
    this.dialogRef.close(this.step === 'success');
  }
}
