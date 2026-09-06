import { Component, ElementRef, Renderer2, ViewChild } from '@angular/core';

@Component({
  selector: 'app-navbar-welcomepage',
  templateUrl: './navbar-welcomepage.component.html',
  styleUrls: ['./navbar-welcomepage.component.css']
})
export class NavbarWelcomepageComponent {
    menuOpen = false;
  
    toggleMenu() {
      this.menuOpen = !this.menuOpen;
    }
  }




