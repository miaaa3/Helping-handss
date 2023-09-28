import { Component, OnInit } from '@angular/core';
import { User } from '../models/user';
import { UserService } from '../services/user.service';
import { follow } from '../models/follow';
import { SearchResult } from '../models/searchResult';
import { FollowService } from '../services/follow.service';
import { HomeComponent } from '../home/home.component';
import { Notification } from '../models/notification';
import { Router } from '@angular/router';
import { TOKEN_KEY, USER_ID } from '../services/token-storage.service';
import { NotificationDTO } from '../models/notificationDTO';

@Component({
  selector: 'app-main-navbar',
  templateUrl: './main-navbar.component.html',
  styleUrls: ['./main-navbar.component.css']
})
export class MainNavbarComponent implements OnInit{
  showDropdown = false;
  showDropdownNotif = false;
  searchKeyword!: string;
  searchResults: SearchResult[] = [];
  isFollowed=false;
  followStatus: any;
  notifications: NotificationDTO[]= [];
  
  constructor(private userService:UserService, private followService: FollowService,private router:Router){}
  ngOnInit(){
    this.getNotif();
  }

  toggleDropdown() {
    this.showDropdown = !this.showDropdown;
  }

  toggleDropdownNotif(){
    this.showDropdownNotif=!this.showDropdownNotif;
  }
  
  searchUsers() {
    this.userService.searchUsers(this.searchKeyword).subscribe(
        (response) => {
          this.searchResults =response;
           if (this.searchKeyword.trim() === '') {
            this.searchResults = [];
          }
        },
        (error) => {
          console.error('Error searching for users:', error);
        }
      );
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
        this.searchUsers()
        
      }
    )
  }

  logout(){
    sessionStorage.removeItem(USER_ID);
    sessionStorage.removeItem(TOKEN_KEY)
    this.router.navigate(['login'])    
  }
  getNotif(){
    this.userService.getUser().subscribe(
      (data: any) => {
        this.notifications=data.notifications
        console.log(this.notifications)
      },
      (error) => {
        console.error('Error getting notifs data:', error);
      }, 
      
    );
  }
  
}
