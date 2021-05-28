import {Component, OnInit} from '@angular/core';
import {JWTTokenService} from '../../../services/auth/JWTTokenService';
import {MeetingsService} from '../../../services/backend.api/meetings.service';
import {MatDialogRef} from '@angular/material/dialog';
import {CreateMeetingDialogComponent} from '../../meetings/create-meeting-dialog/create-meeting-dialog.component';
import {FormControl, FormGroup, Validators} from '@angular/forms';
import {SelectedUsersService} from '../../../services/backend.api/SelectedUsersService';

@Component({
  selector: 'app-create-meeting',
  templateUrl: './create-meeting.component.html',
  styleUrls: ['./create-meeting.component.scss']
})
export class CreateMeetingComponent implements OnInit {
  constructor(
    private tokenService: JWTTokenService,
    private meetingService: MeetingsService,
    private dialogRef: MatDialogRef<CreateMeetingDialogComponent>,
    private selectedUsersService: SelectedUsersService
  ) { }

  form: FormGroup = new FormGroup({
    name: new FormControl('', Validators.compose([Validators.required])),
    description: new FormControl('', Validators.compose([Validators.required])),
  });

  hide = true;

  submitted = false;

  errorMessage = false;

  ngOnInit(): void {
  }

  submit(): void{
    console.log(this.selectedUsersService.getSelectedUsers());

    if (this.form.invalid) {
      return;
    } else {
      this.submitted = true;
      this.meetingService
        .createMeeting(
          this.tokenService.getUsername(),
          this.form.value.name,
          this.selectedUsersService.getSelectedUsers().map((x: string) => x.trim())
        )
        .subscribe(
          res => { console.log(res); },
          _ => {
            this.submitted = false;
            this.errorMessage = true;
            window.location.reload();
          },
          () => { this.dialogRef.close(); }
        );
    }
    window.location.reload();
  }

}
