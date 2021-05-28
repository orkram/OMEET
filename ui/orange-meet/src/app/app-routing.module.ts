import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {SettingsComponent} from './components/settings/settings.component';
import {MeetingsComponent} from './components/meetings/meetings.component';
import {UsersComponentComponent} from './components/users-component/users-component.component';
import {LoginComponent} from './components/login/login.component';
import {RegistrationComponent} from './components/registration/registration.component';
import {AuthorizeGuard} from './services/auth/AuthorizeGuard';
import {CalendarComponent} from './components/calendar/calendar.component';

const routes: Routes = [
  {path: '', redirectTo: 'login', pathMatch: 'full'},
  {path: 'settings', component: SettingsComponent, canActivate: [AuthorizeGuard]},
  {path: 'meetings', component: MeetingsComponent, canActivate: [AuthorizeGuard]},
  {path: 'contacts', component: UsersComponentComponent, canActivate: [AuthorizeGuard]},
  {path: 'calendar', component: CalendarComponent, canActivate: [AuthorizeGuard]}, // TODO ComponentComponent
  {path: 'login', component: LoginComponent},
  {path: 'registration', component: RegistrationComponent}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {}
export const routingComponents  = [SettingsComponent, MeetingsComponent, UsersComponentComponent];
