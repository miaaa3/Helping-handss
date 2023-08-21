import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { RegistrationPageComponent } from './volunteer-registration-page/registration-page.component';
import { RegistrationPageOrganizationComponent } from './organization-registration-page/registration-page-organization.component';
import { LoginVolunteerComponent } from './login-volunteer/login-volunteer.component';
import { WelcomePageComponent } from './welcome-page/welcome-page.component';

const routes: Routes = [
  {path:'sign-up-volunteer', component: RegistrationPageComponent},
  {path:'sign-up-organization', component: RegistrationPageOrganizationComponent},
  {path:'login-volunteer', component: LoginVolunteerComponent},
  {path:'welcome-page', component: WelcomePageComponent},
  {path:'', redirectTo:'welcome-page',component:WelcomePageComponent}

];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
