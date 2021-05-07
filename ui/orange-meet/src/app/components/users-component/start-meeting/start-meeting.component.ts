import {Component, OnInit} from '@angular/core';
import {CreateMeetingDialogComponent} from '../../meetings/create-meeting-dialog/create-meeting-dialog.component';
import {MatDialog} from '@angular/material/dialog';

@Component({
  selector: 'app-start-meeting',
  templateUrl: './start-meeting.component.html',
  styleUrls: ['./start-meeting.component.scss']
})
export class StartMeetingComponent implements OnInit {

  constructor(public dialog: MatDialog) { }

  ngOnInit(): void {
  }

  submit(): void {
    this.dialog.open(CreateMeetingDialogComponent, {});
  }

}
