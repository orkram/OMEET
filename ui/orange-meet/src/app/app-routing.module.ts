import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import {SettingsComponent} from "./components/settings/settings.component";
import {MeetingsComponent} from "./components/meetings/meetings.component";
import {UsersComponentComponent} from "./components/users-component/users-component.component";

const routes: Routes = [
  {path:'settings', component: SettingsComponent},
  {path:'meetings', component: MeetingsComponent},
  {path:'contacts', component: UsersComponentComponent} //TODO ComponentComponent
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
export const routingComponents  = [SettingsComponent, MeetingsComponent, UsersComponentComponent]
