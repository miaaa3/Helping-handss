import { CUSTOM_ELEMENTS_SCHEMA, NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { MatChipsModule } from '@angular/material/chips';
import { MatIconModule } from '@angular/material/icon';
import { MatToolbarModule } from '@angular/material/toolbar';  
import { MatDividerModule } from '@angular/material/divider';  
import { MatButtonModule } from '@angular/material/button';
import { MatCheckboxModule } from '@angular/material/checkbox';
import { MatDialogModule } from '@angular/material/dialog';
import { MatInputModule } from '@angular/material/input';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { MatFormFieldModule } from '@angular/material/form-field';
import { RegistrationPageOrganizationComponent } from './organization-registration-page/registration-page-organization.component';
import { LoginComponent } from './login/login.component';
import { WelcomePageComponent } from './welcome-page/welcome-page.component';
import { NavbarWelcomepageComponent } from './navbar-welcomepage/navbar-welcomepage.component';
import { HTTP_INTERCEPTORS, HttpClientModule } from '@angular/common/http';
import {  RouterModule } from '@angular/router';
import { MainNavbarComponent } from './main-navbar/main-navbar.component';
import { HomeComponent } from './home/home.component';
import { MainSidebarComponent } from './main-sidebar/main-sidebar.component';
import { ToastrModule, ToastrService } from 'ngx-toastr';
import { PostComponent } from './post/post.component';
import { DatePipe } from '@angular/common';
import { SettingsComponent } from './settings/settings.component';
import { UserProfileComponent } from './user-profile/user-profile.component';
import { HttpInterceptorService } from './services/http-interceptor.service';
import { RegistrationPageComponent } from './volunteer-registration-page/registration-page.component';
import { AuthGuard } from './guard/auth.guard';
import { DonateDialogComponent } from './donation/donate-dialog/donate-dialog.component';
import { MyDonationsComponent } from './donation/my-donations/my-donations.component';
import { MessagesComponent } from './messages/messages.component';
import { ConversationListComponent } from './messages/conversation-list/conversation-list.component';
import { ChatWindowComponent } from './messages/chat-window/chat-window.component';

@NgModule({
  declarations: [
    AppComponent,
    RegistrationPageComponent,
    RegistrationPageOrganizationComponent,
    WelcomePageComponent,
    NavbarWelcomepageComponent,
    MainNavbarComponent,
    HomeComponent,
    MainSidebarComponent,
    LoginComponent,
    PostComponent,
    SettingsComponent,
    UserProfileComponent,
    DonateDialogComponent,
    MyDonationsComponent,
    MessagesComponent,
    ConversationListComponent,
    ChatWindowComponent,

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
    MatDialogModule,
    MatInputModule,
    MatProgressSpinnerModule,
    FormsModule,
    MatFormFieldModule,
    MatIconModule,
    HttpClientModule,
    RouterModule,
    ToastrModule.forRoot()

  ],
  providers: [ DatePipe,
    {
      provide: HTTP_INTERCEPTORS,
      useClass: HttpInterceptorService,
      multi: true,
    },
    AuthGuard
  ],
  bootstrap: [AppComponent],
  schemas: [CUSTOM_ELEMENTS_SCHEMA],

})
export class AppModule { }
