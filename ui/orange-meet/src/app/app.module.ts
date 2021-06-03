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
import {MatPaginatorIntl, MatPaginatorModule} from '@angular/material/paginator';
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
import {HTTP_INTERCEPTORS, HttpClient, HttpClientModule} from '@angular/common/http';
import {LoginService} from './services/backend.api/LoginService';
import {RegistrationFormComponent} from './components/registration/registration-form/registration-form.component';
import {AppInterceptor} from './services/AppInterceptor';
import {JWTTokenService} from './services/auth/JWTTokenService';
import {CookieService} from './services/auth/CookieService';
import {LogoutService} from './services/backend.api/LogoutService';
import {MatProgressSpinnerModule} from '@angular/material/progress-spinner';
import {MeetingsService} from './services/backend.api/meetings.service';
import {JoinDialogComponent} from './components/meetings/join-dialog/join-dialog.component';
import {MatDialogModule} from '@angular/material/dialog';
import {CreateMeetingDialogComponent} from './components/meetings/create-meeting-dialog/create-meeting-dialog.component';
import {CommonModule, DatePipe} from '@angular/common';
import {UserService} from './services/backend.api/UserService';
import {MatAutocompleteModule} from '@angular/material/autocomplete';
import {ParticipantsService} from './services/backend.api/ParticipantsService';
import {SelectedUsersService} from './services/backend.api/SelectedUsersService';
import {CreateMeetingComponent} from './components/users-component/create-meeting/create-meeting.component';
import {SettingsService} from './services/backend.api/SettingsService';
import {CalendarModule, DateAdapter} from 'angular-calendar';
import {adapterFactory} from 'angular-calendar/date-adapters/date-fns';
import {CalendarComponent} from './components/calendar/calendar.component';
import {NgbModalModule} from '@ng-bootstrap/ng-bootstrap';
import {FlatpickrModule} from 'angularx-flatpickr';
import {TranslateLoader, TranslateModule, TranslateService} from '@ngx-translate/core';
import {TranslateHttpLoader} from '@ngx-translate/http-loader';
import {PaginatorIntlService} from './model/MatPaginator';
import {JitsiViewComponent} from './jitsi-view/jitsi-view.component';


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
    CreateMeetingComponent,
    CalendarComponent,
    JitsiViewComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    BrowserAnimationsModule,
    MatToolbarModule, MatIconModule,
    MatButtonToggleModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
    MatListModule,
    MatPaginatorModule,
    MatTableModule,
    MatSelectModule,
    MatCheckboxModule,
    MatSortModule,
    MatCardModule,
    MatSlideToggleModule,
    MatExpansionModule,
    MatRippleModule,
    A11yModule,
    MatChipsModule,
    MatProgressSpinnerModule,
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
    TranslateModule.forRoot({
      loader: {
        provide: TranslateLoader,
        useFactory: httpTranslateLoader,
        deps: [HttpClient]
      }
    })
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
    TranslateService,
    { provide: MatPaginatorIntl,
      useClass: PaginatorIntlService,
      deps: [TranslateService]
    },
    {
      provide: HTTP_INTERCEPTORS,
      useClass: AppInterceptor,
      multi: true
    }
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }

export function httpTranslateLoader(http: HttpClient): TranslateHttpLoader {
  return new TranslateHttpLoader(http);
}
