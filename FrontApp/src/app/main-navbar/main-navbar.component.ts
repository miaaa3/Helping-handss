import { Component, ElementRef, HostListener, OnDestroy, OnInit } from '@angular/core';
import { User } from '../models/user';
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

    if (keyword.length === 0) {
      this.searchResults = [];
      return;
    }

    this.searchTerms.next(keyword);
  }

  /** Jumps straight to a chat with this user from the search dropdown. */
  messageUser(user: User) {
    this.showSearchResults = false;
    this.router.navigate(['/messages'], {
      queryParams: { userId: user.id, name: user.name, profile: user.profile }
    });
  }

  follow(userId:number){
    this.followService.follow(userId).subscribe(
      (response )=>{
        const userIndex = this.searchResults.findIndex((user) => user.user.id === userId);
        if (userIndex !== -1) {
          this.searchResults[userIndex].followed = response.followed;
        }
      },
      ()=>{
        this.onSearchInput()
      }
    )
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
