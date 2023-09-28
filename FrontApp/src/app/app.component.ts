import { Component, OnInit } from '@angular/core';
import { TokenStorageService, USER_ID } from './services/token-storage.service';
import { UserService } from './services/user.service'; 
import { User } from './models/user';
import { UserDataService } from './services/user-data.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent  {
  title = 'FrontApp';

}
