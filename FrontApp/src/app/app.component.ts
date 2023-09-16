import { Component, OnInit } from '@angular/core';
import { TokenStorageService } from './services/token-storage.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit {
  title = 'FrontApp';
  isLoggedIn = false;
  roles : string[]=[];

  constructor(private tokenStorageService:TokenStorageService){}

  ngOnInit(): void {
      this.isLoggedIn=!!this.tokenStorageService.getToken();

      if(this.isLoggedIn){
        const volunteer = this.tokenStorageService.getVolunteerEmail();
      }
  }
}
