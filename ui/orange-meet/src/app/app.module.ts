import {NgModule} from '@angular/core';
import {BrowserModule} from '@angular/platform-browser';

import {AppRoutingModule, routingComponents} from './app-routing.module';
import {AppComponent} from './app.component';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {SideNavBarComponent} from './components/side-nav-bar/side-nav-bar.component';
import {MatToolbarModule} from '@angular/material/toolbar';
import {MatIconModule} from '@angular/material/icon';
import {HeaderComponent} from './components/header/header.component';
import {UserInfoComponent} from './components/side-nav-bar/user-info/user-info.component';
import {NavMenuComponent} from './components/side-nav-bar/nav-menu/nav-menu.component';
import {MatButtonToggleModule} from '@angular/material/button-toggle';
import {AddUserComponent} from './components/add-user/add-user.component';
import {MatFormFieldModule} from '@angular/material/form-field';
import {MatInputModule} from '@angular/material/input';
import {MatButtonModule} from '@angular/material/button';
import {UserListComponent} from './components/users-component/user-list/user-list.component';
import {MatPaginatorModule} from '@angular/material/paginator';
import {MatListModule} from '@angular/material/list';
import {MatTableModule} from '@angular/material/table';
import {MatSelectModule} from '@angular/material/select';
import {MatCheckboxModule} from '@angular/material/checkbox';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {MatSortModule} from '@angular/material/sort';
import {StartMeetingComponent} from './components/users-component/start-meeting/start-meeting.component';
import {MatCardModule} from '@angular/material/card';
import {MatSlideToggleModule} from '@angular/material/slide-toggle';
import {MeetingsListComponent} from './components/meetings/meetings-list/meetings-list.component';
import {MatExpansionModule} from '@angular/material/expansion';
import {MatRippleModule} from '@angular/material/core';
import {A11yModule} from '@angular/cdk/a11y';
import {MatChipsModule} from '@angular/material/chips';
import {LoginComponent} from './components/login/login.component';
import {RegistrationComponent} from './components/registration/registration.component';
import {LoginFormComponent} from './components/login/login-form/login-form.component';
import {HTTP_INTERCEPTORS, HttpClientModule} from '@angular/common/http';
import {LoginService} from './services/LoginService';
import {RegistrationFormComponent} from './components/registration/registration-form/registration-form.component';
import {AppInterceptor} from './services/AppInterceptor';
import {JWTTokenService} from './services/JWTTokenService';
import {CookieService} from './services/CookieService';
import {LogoutService} from './services/LogoutService';
import {MatProgressSpinnerModule} from '@angular/material/progress-spinner';
import {MeetingsService} from './services/meetings.service';
import {JoinDialogComponent} from './components/meetings/join-dialog/join-dialog.component';
import {MatDialogModule} from '@angular/material/dialog';
import {CreateMeetingDialogComponent} from './components/meetings/create-meeting-dialog/create-meeting-dialog.component';
import {CommonModule, DatePipe} from '@angular/common';
import {UserService} from './services/UserService';
import {MatAutocompleteModule} from '@angular/material/autocomplete';
import {ParticipantsService} from './services/ParticipantsService';
import {JitsiComponent} from './components/jitsi/jitsi.component';
import {SelectedUsersService} from './services/SelectedUsersService';
import {CreateMeetingComponent} from './components/users-component/create-meeting/create-meeting.component';
import {SettingsService} from './services/SettingsService';
import {CalendarModule, DateAdapter} from 'angular-calendar';
import {adapterFactory} from 'angular-calendar/date-adapters/date-fns';
import {CalendarComponent} from './components/calendar/calendar.component';
import {NgbModalModule} from '@ng-bootstrap/ng-bootstrap';
import {FlatpickrModule} from 'angularx-flatpickr';


@NgModule({
  declarations: [
    AppComponent,
    SideNavBarComponent,
    HeaderComponent,
    UserInfoComponent,
    NavMenuComponent,
    AddUserComponent,
    UserListComponent,
    StartMeetingComponent,
    routingComponents,
    MeetingsListComponent,
    LoginComponent,
    RegistrationComponent,
    LoginFormComponent,
    RegistrationFormComponent,
    JoinDialogComponent,
    CreateMeetingDialogComponent,
    JitsiComponent,
    CreateMeetingComponent,
    CalendarComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    BrowserAnimationsModule,
    MatToolbarModule,
    MatIconModule,
    MatButtonToggleModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
    MatListModule,
    MatPaginatorModule,
    MatTableModule,
    MatSelectModule,
    MatCheckboxModule,
    FormsModule,
    MatSortModule,
    MatCardModule,
    MatSlideToggleModule,
    MatExpansionModule,
    MatRippleModule,
    A11yModule,
    MatChipsModule,
    MatProgressSpinnerModule,
    FormsModule,
    ReactiveFormsModule,
    HttpClientModule,
    MatDialogModule,
    MatAutocompleteModule,
    CommonModule,
    FormsModule,
    NgbModalModule,
    BrowserAnimationsModule,
    FlatpickrModule.forRoot(),
    CalendarModule.forRoot({
      provide: DateAdapter,
      useFactory: adapterFactory,
    }),
  ],
  providers: [
    MeetingsService,
    LoginService,
    JWTTokenService,
    CookieService,
    LogoutService,
    UserService,
    DatePipe,
    SettingsService,
    SelectedUsersService,
    ParticipantsService,
    {
      provide: HTTP_INTERCEPTORS,
      useClass: AppInterceptor,
      multi: true
    }
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }