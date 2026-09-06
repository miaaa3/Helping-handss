/** Mirrors backend NotificationResponse DTO. */
export interface Notification {
  id: number;
  message: string;
  notificationType: string;
  createdAt: Date;
  isRead: boolean;
  /** Frontend route to navigate to when this notification is clicked, e.g. "/dashboard" or "/user-profile/12". */
  link: string;
  actorId: number;
  actorName: string;
  actorProfile: string;
}
