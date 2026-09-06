import { Component, OnInit } from '@angular/core';
import { UserDataService } from '../services/user-data.service';
import { UserService } from '../services/user.service';

@Component({
  selector: 'app-main-sidebar',
  templateUrl: './main-sidebar.component.html',
  styleUrls: ['./main-sidebar.component.css']
})
export class MainSidebarComponent implements OnInit {
  isAdmin = false;

  constructor(private userDataService: UserDataService, private userService: UserService) {}

  ngOnInit(): void {
    const cachedUser = this.userDataService.getUser();
    if (cachedUser) {
      this.isAdmin = cachedUser.role === 'ADMIN';
      return;
    }

    this.userService.getUser().subscribe({
      next: (data: any) => {
        const user = data.user;
        this.userDataService.setUser(user);
        this.isAdmin = user?.role === 'ADMIN';
      },
      error: (err) => console.error('Error getting user data:', err)
    });
  }
}
