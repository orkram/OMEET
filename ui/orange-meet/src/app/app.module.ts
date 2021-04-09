import {NgModule} from '@angular/core';
import {BrowserModule} from '@angular/platform-browser';

import {AppRoutingModule, routingComponents} from './app-routing.module';
import {AppComponent} from './app.component';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {SideNavBarComponent} from './components/side-nav-bar/side-nav-bar.component';
import {MatToolbarModule} from "@angular/material/toolbar";
import {MatIconModule} from "@angular/material/icon";
import {HeaderComponent} from "./components/header/header.component";
import {UserInfoComponent} from './components/side-nav-bar/user-info/user-info.component';
import {NavMenuComponent} from './components/side-nav-bar/nav-menu/nav-menu.component';
import {MatButtonToggleModule} from "@angular/material/button-toggle";
import {AddUserComponent} from './components/add-user/add-user.component';
import {MatFormFieldModule} from '@angular/material/form-field';
import {MatInputModule} from "@angular/material/input";
import { MatButtonModule} from "@angular/material/button";
import { UserListComponent } from './components/users-component/user-list/user-list.component';
import { FindUserFieldComponent } from './components/users-component/user-list/find-user-field/find-user-field.component';
import {MatPaginatorModule} from '@angular/material/paginator';
import {MatListModule} from "@angular/material/list";
import {MatTableModule} from "@angular/material/table";
import {MatSelectModule} from "@angular/material/select";
import {MatCheckboxModule} from "@angular/material/checkbox";
import {FormsModule} from "@angular/forms";
import {MatSortModule} from "@angular/material/sort";
import { StartMeetingComponent } from './components/users-component/start-meeting/start-meeting.component';
import {MatCardModule} from "@angular/material/card";
import {MatSlideToggleModule} from "@angular/material/slide-toggle";
import { MeetingsListComponent } from './components/meetings/meetings-list/meetings-list.component';
import {MatExpansionModule} from '@angular/material/expansion';
import {MatRippleModule} from "@angular/material/core";
import {A11yModule} from "@angular/cdk/a11y";
import { FindMeetingComponent } from './components/meetings/meetings-list/find-meeting/find-meeting.component';
import { MatChipsModule} from "@angular/material/chips";


@NgModule({
  declarations: [
    AppComponent,
    SideNavBarComponent,
    HeaderComponent,
    UserInfoComponent,
    NavMenuComponent,
    AddUserComponent,
    UserListComponent,
    FindUserFieldComponent,
    StartMeetingComponent,
    routingComponents,
    MeetingsListComponent,
    FindMeetingComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
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
    MatChipsModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
