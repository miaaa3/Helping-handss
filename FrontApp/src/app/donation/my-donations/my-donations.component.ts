import { Component, OnInit } from '@angular/core';
import { Donation } from '../../models/donation';
import { DonationService } from '../../services/donation.service';

@Component({
  selector: 'app-my-donations',
  templateUrl: './my-donations.component.html',
  styleUrls: ['./my-donations.component.css']
})
export class MyDonationsComponent implements OnInit {
  donations: Donation[] = [];
  loading = true;
  errorMessage: string | null = null;

  constructor(private donationService: DonationService) {}

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
}
