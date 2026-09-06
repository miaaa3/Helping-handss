import { Component, OnInit } from '@angular/core';
import { UserService } from '../services/user.service';

/** Routes to the role-appropriate dashboard once the current user's role is known. */
@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css']
})
export class DashboardComponent implements OnInit {
  loading = true;
  role: string | null = null;

  constructor(private userService: UserService) {}

  ngOnInit(): void {
    this.userService.getUser().subscribe({
      next: (data: any) => {
        this.role = data?.user?.role ?? null;
        this.loading = false;
      },
      error: (err) => {
        console.error('Error getting user data:', err);
        this.loading = false;
      }
    });
  }
}
