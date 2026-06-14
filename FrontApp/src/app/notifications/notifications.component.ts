import { Component, HostListener, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { NotificationService } from '../services/notification.service';
import { Notification } from '../models/notification';

@Component({
  selector: 'app-notifications',
  templateUrl: './notifications.component.html',
  styleUrls: ['./notifications.component.css']
})
export class NotificationsComponent implements OnInit {
  notifications: Notification[] = [];
  loading = false;
  initialLoad = true;
  hasMore = true;

  private readonly pageSize = 20;
  private page = 0;

  constructor(private notificationService: NotificationService, private router: Router) {}

  ngOnInit(): void {
    this.loadNotifications(true);
  }

  /** Loads notifications page by page. Pass reset=true to reload from the first page. */
  loadNotifications(reset: boolean = false): void {
    if (this.loading || (!reset && !this.hasMore)) return;

    if (reset) {
      this.page = 0;
      this.hasMore = true;
      this.notifications = [];
    }

    this.loading = true;
    this.notificationService.getNotifications(this.page, this.pageSize).subscribe({
      next: (data) => {
        this.notifications.push(...data.content);
        this.hasMore = !data.last;
        this.page++;
        this.loading = false;
        this.initialLoad = false;
      },
      error: (err) => {
        console.error('Error loading notifications:', err);
        this.loading = false;
        this.initialLoad = false;
      }
    });
  }

  /** Loads the next page once the user nears the bottom of the page. */
  @HostListener('window:scroll')
  onWindowScroll(): void {
    const threshold = 400;
    const reachedBottom = window.innerHeight + window.scrollY >= document.body.offsetHeight - threshold;
    if (reachedBottom) {
      this.loadNotifications();
    }
  }

  markAllAsRead(): void {
    this.notificationService.markAllAsRead().subscribe({
      next: () => this.notifications.forEach(n => (n.isRead = true)),
      error: (err) => console.error('Error marking all as read:', err)
    });
  }

  /** Marks the notification read (if needed) and navigates to its target. */
  openNotification(notification: Notification): void {
    if (!notification.isRead) {
      this.notificationService.markAsRead(notification.id).subscribe({
        next: () => (notification.isRead = true),
        error: (err) => console.error('Error marking notification as read:', err)
      });
    }
    this.router.navigateByUrl(notification.link || '/notifications');
  }

  /** Picks an icon for each notification type. */
  iconFor(type: string): string {
    switch (type) {
      case 'LIKE': return '👍';
      case 'COMMENT': return '💬';
      case 'FOLLOW': return '➕';
      case 'DONATION': return '💝';
      case 'APPLICATION': return '📋';
      case 'APPLICATION_DECISION': return '✅';
      default: return '🔔';
    }
  }

  get unreadCount(): number {
    return this.notifications.filter(n => !n.isRead).length;
  }
}
