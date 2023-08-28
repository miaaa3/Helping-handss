import { NgModule } from '@angular/core';
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

@NgModule({
  declarations: [
    AppComponent,
    RegistrationPageComponent,
    RegistrationPageOrganizationComponent,
    LoginVolunteerComponent,
    WelcomePageComponent,
    NavbarWelcomepageComponent
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
    HttpClientModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
