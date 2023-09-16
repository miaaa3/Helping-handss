import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { RegistrationPageComponent } from './volunteer-registration-page/registration-page.component';
import { RegistrationPageOrganizationComponent } from './organization-registration-page/registration-page-organization.component';
import { LoginVolunteerComponent } from './login-volunteer/login-volunteer.component';
import { WelcomePageComponent } from './welcome-page/welcome-page.component';
import { HomePageComponent } from './home-page/home-page.component';
import { HomeComponent } from './home/home.component';
import { SettingsComponent } from './settings/settings.component';
import { UserProfileComponent } from './user-profile/user-profile.component';

const routes: Routes = [
  {path:'sign-up-volunteer', component: RegistrationPageComponent},
  {path:'sign-up-organization', component: RegistrationPageOrganizationComponent},
  {path:'login', component:LoginVolunteerComponent},
  {path:'welcome-page', component: WelcomePageComponent},
  {path:'', redirectTo:'welcome-page',pathMatch: 'full'},
  {path:'home',component:HomeComponent },
  {path:'settings',component:SettingsComponent },
  {path: 'user-profile', component:UserProfileComponent},


];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
})
export class AppRoutingModule { }
