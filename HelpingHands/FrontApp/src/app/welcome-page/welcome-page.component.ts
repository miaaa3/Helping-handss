import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from 'src/environments/environment';

interface PublicOpportunity {
  id: number;
  title: string;
  description: string;
  category: string;
  date: string;
  location: string;
  neededVolunteers: number;
  applicantCount: number;
  organizationName: string;
  organizationProfile: string;
  status: string;
}

@Component({
  selector: 'app-welcome-page',
  templateUrl: './welcome-page.component.html',
  styleUrls: ['./welcome-page.component.css']
})
export class WelcomePageComponent implements OnInit {
  opportunities: PublicOpportunity[] = [];
  loading = true;

  constructor(private http: HttpClient) {}

  ngOnInit(): void {
    this.http.get<PublicOpportunity[]>(`${environment.apiUrl}public/opportunities`).subscribe({
      next: (data) => {
        this.opportunities = data.slice(0, 3);
        this.loading = false;
      },
      error: () => {
        this.loading = false;
      }
    });
  }

  formatDate(dateStr: string): string {
    if (!dateStr) return '';
    const d = new Date(dateStr);
    return d.toLocaleDateString('en-US', { month: 'short', day: 'numeric', year: 'numeric' });
  }

  spotsLeft(o: PublicOpportunity): number {
    return Math.max(0, o.neededVolunteers - o.applicantCount);
  }

  categoryLabel(cat: string): string {
    const map: Record<string, string> = {
      EDUCATION: 'Education', ENVIRONMENT: 'Environment', HEALTH: 'Health',
      COMMUNITY: 'Community', ANIMAL_WELFARE: 'Animal Welfare',
      DISASTER_RELIEF: 'Disaster Relief', ARTS: 'Arts & Culture', OTHER: 'Other'
    };
    return map[cat] || cat;
  }

  categoryColor(cat: string): string {
    const map: Record<string, string> = {
      EDUCATION: 'bg-blue-100 text-blue-700',
      ENVIRONMENT: 'bg-green-100 text-green-700',
      HEALTH: 'bg-red-100 text-red-700',
      COMMUNITY: 'bg-yellow-100 text-yellow-700',
      ANIMAL_WELFARE: 'bg-orange-100 text-orange-700',
      DISASTER_RELIEF: 'bg-purple-100 text-purple-700',
      ARTS: 'bg-pink-100 text-pink-700',
      OTHER: 'bg-gray-100 text-gray-700'
    };
    return map[cat] || 'bg-gray-100 text-gray-700';
  }
}
