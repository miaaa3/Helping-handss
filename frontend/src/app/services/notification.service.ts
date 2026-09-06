import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';
import { Notification } from '../models/notification';
import { PageResponse } from '../models/page';

@Injectable({
  providedIn: 'root'
})
export class NotificationService {
  private notificationUrl: string;

  constructor(private http: HttpClient) {
    this.notificationUrl = `${environment.apiUrl}api/notifications`;
  }

  getNotifications(page: number = 0, size: number = 20): Observable<PageResponse<Notification>> {
    return this.http.get<PageResponse<Notification>>(`${this.notificationUrl}?page=${page}&size=${size}`);
  }

  getUnreadCount(): Observable<{ count: number }> {
    return this.http.get<{ count: number }>(`${this.notificationUrl}/unread-count`);
  }

  markAsRead(id: number): Observable<Notification> {
    return this.http.put<Notification>(`${this.notificationUrl}/${id}/read`, {});
  }

  markAllAsRead(): Observable<void> {
    return this.http.put<void>(`${this.notificationUrl}/read-all`, {});
  }
}
