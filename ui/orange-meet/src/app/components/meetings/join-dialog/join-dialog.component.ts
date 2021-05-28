import {Component, OnInit} from '@angular/core';
import {FormControl, FormGroup, Validators} from '@angular/forms';
import {JWTTokenService} from '../../../services/auth/JWTTokenService';
import {SettingsService} from '../../../services/backend.api/SettingsService';

@Component({
  selector: 'app-join-dialog',
  templateUrl: './join-dialog.component.html',
  styleUrls: ['./join-dialog.component.scss']
})
export class JoinDialogComponent implements OnInit {

  constructor(private tokenService: JWTTokenService, private settings: SettingsService) { }

  form: FormGroup = new FormGroup({
    id: new FormControl('', Validators.compose([Validators.required])),
  });

  hide = true;

  submitted = false;

  errorMessage = false;

  ngOnInit(): void {
  }

  submit(): void{
    let micMuted = false;
    let VidMuted = false;
    this.settings.getSettings(this.tokenService.getUsername()).subscribe(
      next => {
        micMuted = !next.defaultMicOn;
        VidMuted = next.defaultCamOn;
      }
    );
    if (this.form.invalid) {
      return;
    } else {
      this.submitted = true;
      window.open(`https://130.61.186.61/${this.form.value.id}#userInfo.displayName=%22${this.tokenService.getEmail()}%22&config.subject="Test meeting"&config.startWithVideoMuted=${VidMuted}&config.startSilent=false`);
    }
  }

}

