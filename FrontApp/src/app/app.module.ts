import { CUSTOM_ELEMENTS_SCHEMA, NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { MatChipsModule } from '@angular/material/chips';
import { MatIconModule } from '@angular/material/icon';
import { MatToolbarModule } from '@angular/material/toolbar';  
import { MatDividerModule } from '@angular/material/divider';  
import { MatButtonModule } from '@angular/material/button';    
import { MatCheckboxModule } from '@angular/material/checkbox';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { RegistrationPageComponent } from './volunteer-registration-page/registration-page.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { MatFormFieldModule } from '@angular/material/form-field';
import { RegistrationPageOrganizationComponent } from './organization-registration-page/registration-page-organization.component';
import { LoginVolunteerComponent } from './login-volunteer/login-volunteer.component';
import { WelcomePageComponent } from './welcome-page/welcome-page.component';
import { NavbarWelcomepageComponent } from './navbar-welcomepage/navbar-welcomepage.component';
import { HttpClientModule } from '@angular/common/http';
import { Router, RouterModule } from '@angular/router';
import { JwtModule } from '@auth0/angular-jwt';
import { HomePageComponent } from './home-page/home-page.component';
import { MainNavbarComponent } from './main-navbar/main-navbar.component';
import { HomeComponent } from './home/home.component';
import { MainSidebarComponent } from './main-sidebar/main-sidebar.component';
import { ToastrModule, ToastrService } from 'ngx-toastr';
import { PostComponent } from './post/post.component';
import { DatePipe } from '@angular/common';
import { SettingsComponent } from './settings/settings.component';
import jwtDecode from 'jwt-decode';
import { UserProfileComponent } from './user-profile/user-profile.component';

@NgModule({
  declarations: [
    AppComponent,
    RegistrationPageComponent,
    RegistrationPageOrganizationComponent,
    WelcomePageComponent,
    NavbarWelcomepageComponent,
    HomePageComponent,
    MainNavbarComponent,
    HomeComponent,
    MainSidebarComponent,
    LoginVolunteerComponent,
    PostComponent,
    SettingsComponent,
    UserProfileComponent,
    
    
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    BrowserAnimationsModule,
    MatChipsModule,
    MatIconModule,
    MatToolbarModule,
    MatDividerModule,
    ReactiveFormsModule,
    MatButtonModule,
    MatCheckboxModule,
    FormsModule,
    MatFormFieldModule,
    MatIconModule,
    HttpClientModule,
    RouterModule,
    ToastrModule.forRoot()
    
  ],
  providers: [ DatePipe],
  bootstrap: [AppComponent],
  schemas: [CUSTOM_ELEMENTS_SCHEMA],

})
export class AppModule { }
