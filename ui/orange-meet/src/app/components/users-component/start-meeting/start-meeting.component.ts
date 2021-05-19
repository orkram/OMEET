import {Component, OnInit} from '@angular/core';
import {MatDialog} from '@angular/material/dialog';
import {CreateMeetingComponent} from '../create-meeting/create-meeting.component';

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
    this.dialog.open(CreateMeetingComponent, {});
  }

}
