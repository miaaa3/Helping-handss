import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { RegistrationPageComponent } from './volunteer-registration-page/registration-page.component';
import { RegistrationPageOrganizationComponent } from './organization-registration-page/registration-page-organization.component';
import { WelcomePageComponent } from './welcome-page/welcome-page.component';
import { HomeComponent } from './home/home.component';
import { SettingsComponent } from './settings/settings.component';
import { UserProfileComponent } from './user-profile/user-profile.component';
import { AuthGuard } from './guard/auth.guard';
import { LoginComponent } from './login/login.component';
import { MyDonationsComponent } from './donation/my-donations/my-donations.component';
import { MessagesComponent } from './messages/messages.component';
import { OpportunitiesComponent } from './opportunities/opportunities.component';
import { NotificationsComponent } from './notifications/notifications.component';
import { DashboardComponent } from './dashboard/dashboard.component';
import { AdminDashboardComponent } from './admin-dashboard/admin-dashboard.component';
import { AdminGuard } from './guard/admin.guard';

const routes: Routes = [
  {path:'sign-up-volunteer', component: RegistrationPageComponent},
  {path:'sign-up-organization', component: RegistrationPageOrganizationComponent},
  {path:'login', component:LoginComponent},
  {path:'welcome-page', component: WelcomePageComponent},
  {path:'', redirectTo:'welcome-page',pathMatch: 'full'},
  {path:'home',component:HomeComponent,canActivate:[AuthGuard] },
  {path:'settings',component:SettingsComponent, canActivate:[AuthGuard] },
  {path: 'user-profile', component:UserProfileComponent, canActivate:[AuthGuard]},
  {path: 'user-profile/:id', component:UserProfileComponent, canActivate:[AuthGuard]},
  {path: 'my-donations', component:MyDonationsComponent, canActivate:[AuthGuard]},
  {path: 'messages', component:MessagesComponent, canActivate:[AuthGuard]},
  {path: 'opportunities', component:OpportunitiesComponent, canActivate:[AuthGuard]},
  {path: 'notifications', component:NotificationsComponent, canActivate:[AuthGuard]},
  {path: 'dashboard', component:DashboardComponent, canActivate:[AuthGuard]},
  {path: 'admin', component:AdminDashboardComponent, canActivate:[AuthGuard, AdminGuard]},


];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
})
export class AppRoutingModule { }
