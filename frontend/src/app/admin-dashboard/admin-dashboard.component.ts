import { Component } from '@angular/core';

type AdminTab = 'organizations' | 'users' | 'content' | 'donations';

/** Admin panel: organization verification, user management, content moderation, donation activity. */
@Component({
  selector: 'app-admin-dashboard',
  templateUrl: './admin-dashboard.component.html',
  styleUrls: ['./admin-dashboard.component.css']
})
export class AdminDashboardComponent {
  activeTab: AdminTab = 'organizations';

  tabs: { id: AdminTab; label: string }[] = [
    { id: 'organizations', label: 'Organizations' },
    { id: 'users', label: 'Users' },
    { id: 'content', label: 'Content' },
    { id: 'donations', label: 'Donations' }
  ];
}
