import {Component, OnInit} from '@angular/core';
import {Router} from '@angular/router';
import {JWTTokenService} from '../../services/auth/JWTTokenService';
import {MatDialog} from '@angular/material/dialog';
import {JoinDialogComponent} from './join-dialog/join-dialog.component';
import {CreateMeetingDialogComponent} from './create-meeting-dialog/create-meeting-dialog.component';

@Component({
  selector: 'app-meetings',
  templateUrl: './meetings.component.html',
  styleUrls: ['./meetings.component.scss']
})
export class MeetingsComponent implements OnInit {

  constructor(private router: Router, private jwtService: JWTTokenService, public dialog: MatDialog) { }

  ngOnInit(): void {
    if (!this.jwtService.isAccessTokenExpired()){
      this.router.navigateByUrl('/meetings');
    }
  }
  joinMeeting(): void{
    this.dialog.open(JoinDialogComponent, {});
  }

  createMeeting(): void{
    this.dialog.open(CreateMeetingDialogComponent, {});
  }

}
