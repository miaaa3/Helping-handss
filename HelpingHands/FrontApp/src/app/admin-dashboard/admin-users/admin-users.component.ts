import { Component, OnInit } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { ToastrService } from 'ngx-toastr';
import { ConfirmDialogComponent } from '../../helpers/confirm-dialog/confirm-dialog.component';
import { AdminUser } from '../../models/admin';
import { AdminService } from '../../services/admin.service';

/** Admin tab: list all accounts and enable/disable access. Admin accounts can't be disabled. */
@Component({
  selector: 'app-admin-users',
  templateUrl: './admin-users.component.html',
  styleUrls: ['./admin-users.component.css']
})
export class AdminUsersComponent implements OnInit {
  users: AdminUser[] = [];
  loading = true;
  updatingId: number | null = null;

  constructor(private adminService: AdminService, private toastr: ToastrService, private dialog: MatDialog) {}

  ngOnInit(): void {
    this.load();
  }

  load(): void {
    this.loading = true;
    this.adminService.listUsers().subscribe({
      next: (users) => {
        this.users = users;
        this.loading = false;
      },
      error: (err) => {
        console.error('Error loading users:', err);
        this.toastr.error('Could not load users.');
        this.loading = false;
      }
    });
  }

  toggleEnabled(user: AdminUser): void {
    if (user.role === 'ADMIN' || this.updatingId) return;

    if (!user.enabled) {
      this.setEnabled(user, true);
      return;
    }

    const dialogRef = this.dialog.open(ConfirmDialogComponent, {
      width: '380px',
      data: {
        title: 'Disable account?',
        message: `Disable "${user.name}"? They won't be able to sign in until re-enabled.`,
        confirmLabel: 'Disable',
        destructive: true
      }
    });

    dialogRef.afterClosed().subscribe((confirmed: boolean) => {
      if (confirmed) this.setEnabled(user, false);
    });
  }

  private setEnabled(user: AdminUser, enabled: boolean): void {
    this.updatingId = user.id;
    this.adminService.setUserEnabled(user.id, enabled).subscribe({
      next: (updated) => {
        user.enabled = updated.enabled;
        this.updatingId = null;
        this.toastr.success(enabled ? 'Account enabled.' : 'Account disabled.');
      },
      error: (err) => {
        console.error('Error updating account status:', err);
        this.toastr.error('Could not update account status.');
        this.updatingId = null;
      }
    });
  }
}
