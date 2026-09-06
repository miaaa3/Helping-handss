import { Component, ElementRef, HostListener, OnDestroy, OnInit } from '@angular/core';
import { User } from '../models/user';
import { Volunteer } from '../models/volunteer';
import { UserService } from '../services/user.service';
import { follow } from '../models/follow';
import { SearchResult } from '../models/searchResult';
import { FollowService } from '../services/follow.service';
import { HomeComponent } from '../home/home.component';
import { Notification } from '../models/notification';
import { Router } from '@angular/router';
import { TokenStorageService } from '../services/token-storage.service';
import { NotificationService } from '../services/notification.service';
import { Subject, Subscription } from 'rxjs';
import { debounceTime, distinctUntilChanged, switchMap } from 'rxjs/operators';

@Component({
  selector: 'app-main-navbar',
  templateUrl: './main-navbar.component.html',
  styleUrls: ['./main-navbar.component.css']
})
export class MainNavbarComponent implements OnInit, OnDestroy {
  showDropdown = false;
  showDropdownNotif = false;
  showSearchResults = false;
  searchKeyword = '';
  searchResults: SearchResult[] = [];
  isFollowed=false;
  followStatus: any;
  notifications: Notification[]= [];
  unreadCount = 0;
  currentUser: Volunteer = {} as Volunteer;
  highlightedIndex = -1;

  private searchTerms = new Subject<string>();
  private searchSubscription?: Subscription;

  constructor(
    private userService: UserService,
    private followService: FollowService,
    private notificationService: NotificationService,
    private router: Router,
    private tokenStorage: TokenStorageService,
    private elementRef: ElementRef
  ) {}

  ngOnInit() {
    this.getNotif();
    this.getCurrentUser();

    // Debounce keystrokes so we don't hit the backend on every character typed.
    this.searchSubscription = this.searchTerms
      .pipe(
        debounceTime(300),
        distinctUntilChanged(),
        switchMap((keyword) => this.userService.searchUsers(keyword))
      )
      .subscribe({
        next: (response) => (this.searchResults = response),
        error: (error) => console.error('Error searching for users:', error)
      });
  }

  ngOnDestroy() {
    this.searchSubscription?.unsubscribe();
  }

  @HostListener('document:click', ['$event'])
  onDocumentClick(event: MouseEvent) {
    if (!this.elementRef.nativeElement.contains(event.target)) {
      this.showSearchResults = false;
      this.showDropdown = false;
      this.showDropdownNotif = false;
    }
  }

  toggleDropdown() {
    this.showDropdown = !this.showDropdown;
  }

  toggleDropdownNotif(){
    this.showDropdownNotif=!this.showDropdownNotif;
  }

  onSearchInput() {
    const keyword = this.searchKeyword.trim();
    this.showSearchResults = keyword.length > 0;
    this.highlightedIndex = -1;

    if (keyword.length === 0) {
      this.searchResults = [];
      return;
    }

    this.searchTerms.next(keyword);
  }

  onSearchKeydown(event: KeyboardEvent): void {
    if (!this.showSearchResults || this.searchResults.length === 0) return;

    if (event.key === 'ArrowDown') {
      event.preventDefault();
      this.highlightedIndex = Math.min(this.highlightedIndex + 1, this.searchResults.length - 1);
    } else if (event.key === 'ArrowUp') {
      event.preventDefault();
      this.highlightedIndex = Math.max(this.highlightedIndex - 1, 0);
    } else if (event.key === 'Enter' && this.highlightedIndex >= 0) {
      event.preventDefault();
      const user = this.searchResults[this.highlightedIndex].user;
      this.showSearchResults = false;
      this.searchKeyword = '';
      this.router.navigate(['/user-profile', user.id]);
    } else if (event.key === 'Escape') {
      this.showSearchResults = false;
      this.highlightedIndex = -1;
    }
  }

  roleBadgeClass(role: string): string {
    if (role === 'ORGANIZATION') return 'bg-blue-100 text-blue-700';
    if (role === 'VOLUNTEER') return 'bg-green-100 text-green-700';
    return 'bg-gray-100 text-gray-600';
  }

  roleLabel(role: string): string {
    if (role === 'ORGANIZATION') return 'Org';
    if (role === 'VOLUNTEER') return 'Vol';
    return role;
  }

  /** Jumps straight to a chat with this user from the search dropdown. */
  messageUser(user: User) {
    this.showSearchResults = false;
    this.router.navigate(['/messages'], {
      queryParams: { userId: user.id, name: user.name, profile: user.profile }
    });
  }

  follow(userId: number): void {
    this.followService.follow(userId).subscribe({
      next: (response) => {
        const idx = this.searchResults.findIndex((r) => r.user.id === userId);
        if (idx !== -1) {
          this.searchResults[idx].followed = response.followed;
          (this.searchResults[idx] as any).followStatus = response.status;
        }
      },
      error: () => this.onSearchInput()
    });
  }

  followLabel(result: SearchResult): string {
    const status = (result as any).followStatus;
    if (status === 'PENDING') return 'Pending';
    if (result.followed) return 'Following';
    return 'Follow';
  }

  followBtnClass(result: SearchResult): string {
    const status = (result as any).followStatus;
    if (status === 'PENDING') return 'bg-yellow-400 text-white';
    if (result.followed) return 'bg-gray-200 text-gray-700 hover:bg-red-100 hover:text-red-600';
    return 'bg-green text-white hover:bg-green-dark';
  }

  logout(){
    this.tokenStorage.clear();
    this.router.navigate(['login'])
  }
  getNotif(){
    this.notificationService.getNotifications(0, 5).subscribe(
      (page) => {
        this.notifications = page.content;
      },
      (error) => {
        console.error('Error getting notifications:', error);
      },
    );
    this.refreshUnreadCount();
  }

  /** Loads the logged-in user's own profile picture for the account menu icon. */
  getCurrentUser(){
    this.userService.getUser().subscribe(
      (data: any) => {
        this.currentUser = data.user;
      },
      (error) => {
        console.error('Error getting current user:', error);
      }
    );
  }

  refreshUnreadCount(){
    this.notificationService.getUnreadCount().subscribe(
      (res) => this.unreadCount = res.count,
      (error) => console.error('Error getting unread count:', error),
    );
  }

  /** Marks the notification read (if needed) and navigates to its target. */
  openNotification(notification: Notification){
    this.showDropdownNotif = false;
    if (!notification.isRead) {
      this.notificationService.markAsRead(notification.id).subscribe({
        next: () => {
          notification.isRead = true;
          this.refreshUnreadCount();
        },
        error: (error) => console.error('Error marking notification as read:', error),
      });
    }
    this.router.navigateByUrl(notification.link || '/notifications');
  }

}
