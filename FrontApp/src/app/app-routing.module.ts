import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { RegistrationPageComponent } from './volunteer-registration-page/registration-page.component';
import { RegistrationPageOrganizationComponent } from './organization-registration-page/registration-page-organization.component';
import { LoginVolunteerComponent } from './login-volunteer/login-volunteer.component';
import { WelcomePageComponent } from './welcome-page/welcome-page.component';
import { HomePageComponent } from './home-page/home-page.component';
import { HomeComponent } from './home/home.component';
import { roleGuardGuard } from './guard/role-guard.guard';

const routes: Routes = [
  {path:'sign-up-volunteer', component: RegistrationPageComponent},
  {path:'sign-up-organization', component: RegistrationPageOrganizationComponent},
  {path:'login', component:LoginVolunteerComponent},
  {path:'welcome-page', component: WelcomePageComponent},
  {path:'', redirectTo:'welcome-page',pathMatch: 'full'},
  {path:'home' , component:HomePageComponent},
  {path:'home2',component:HomeComponent ,canActivate: [roleGuardGuard('ADMIN')]}

];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
})
export class AppRoutingModule { }
