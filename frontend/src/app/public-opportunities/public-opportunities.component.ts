import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from 'src/environments/environment';

export interface PublicOpportunity {
  id: number;
  title: string;
  description: string;
  category: string;
  date: string;
  location: string;
  neededVolunteers: number;
  applicantCount: number;
  status: string;
  organizationId: number;
  organizationName: string;
  organizationProfile: string;
}

@Component({
  selector: 'app-public-opportunities',
  templateUrl: './public-opportunities.component.html',
  styleUrls: ['./public-opportunities.component.css']
})
export class PublicOpportunitiesComponent implements OnInit {
  opportunities: PublicOpportunity[] = [];
  filtered: PublicOpportunity[] = [];
  loading = true;
  selectedCategory = '';

  readonly categories = ['COMMUNITY', 'ENVIRONMENT', 'EDUCATION', 'HEALTH', 'ANIMALS', 'DISASTER_RELIEF'];
  readonly categoryLabels: Record<string, string> = {
    COMMUNITY: 'Community', ENVIRONMENT: 'Environment', EDUCATION: 'Education',
    HEALTH: 'Health', ANIMALS: 'Animals', DISASTER_RELIEF: 'Disaster Relief'
  };

  constructor(private http: HttpClient) {}

  ngOnInit(): void {
    this.http.get<PublicOpportunity[]>(`${environment.apiUrl}public/opportunities`).subscribe({
      next: (data) => {
        this.opportunities = data;
        this.filtered = data;
        this.loading = false;
      },
      error: () => { this.loading = false; }
    });
  }

  filterByCategory(cat: string): void {
    this.selectedCategory = cat;
    this.filtered = cat ? this.opportunities.filter(o => o.category === cat) : this.opportunities;
  }

  spotsLeft(o: PublicOpportunity): number {
    return Math.max(0, o.neededVolunteers - o.applicantCount);
  }

  formatDate(d: string): string {
    return new Date(d).toLocaleDateString('en-GB', { day: 'numeric', month: 'short', year: 'numeric' });
  }

  categoryLabel(cat: string): string {
    return this.categoryLabels[cat] ?? cat;
  }
}
