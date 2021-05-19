import {Component, OnInit} from '@angular/core';
import {FormControl, FormGroup, Validators} from '@angular/forms';
import {JWTTokenService} from '../../../services/JWTTokenService';
import {MeetingsService} from '../../../services/meetings.service';
import {MatDialogRef} from '@angular/material/dialog';

@Component({
  selector: 'app-create-meeting-dialog',
  templateUrl: './create-meeting-dialog.component.html',
  styleUrls: ['./create-meeting-dialog.component.scss']
})
export class CreateMeetingDialogComponent implements OnInit {

  constructor(
    private tokenService: JWTTokenService,
    private meetingService: MeetingsService,
    private dialogRef: MatDialogRef<CreateMeetingDialogComponent>
  ) { }

  form: FormGroup = new FormGroup({
    name: new FormControl('', Validators.compose([Validators.required])),
    description: new FormControl('', Validators.compose([Validators.required])),
    participants: new FormControl(''),
  });

  hide = true;

  submitted = false;

  errorMessage = false;

  ngOnInit(): void {
  }

  submit(): void{
    if (this.form.invalid) {
      return;
    } else {
      this.submitted = true;
      this.meetingService
        .createMeeting(
          this.tokenService.getUsername(),
          this.form.value.name,
          this.form.value.participants.split(',').map((x: string) => x.trim())
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
