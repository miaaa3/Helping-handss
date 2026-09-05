import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from 'src/environments/environment';

export interface PublicOrganization {
  id: number;
  name: string;
  profile: string;
  description: string;
  type: string;
  founder: string;
  website: string;
  address: string;
}

@Component({
  selector: 'app-public-organizations',
  templateUrl: './public-organizations.component.html',
  styleUrls: ['./public-organizations.component.css']
})
export class PublicOrganizationsComponent implements OnInit {
  organizations: PublicOrganization[] = [];
  loading = true;

  constructor(private http: HttpClient) {}

  ngOnInit(): void {
    this.http.get<PublicOrganization[]>(`${environment.apiUrl}public/organizations`).subscribe({
      next: (data) => {
        this.organizations = data;
        this.loading = false;
      },
      error: () => { this.loading = false; }
    });
  }
}
