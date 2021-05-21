import {Component, OnInit} from '@angular/core';
import {SettingsService} from '../../services/SettingsService';
import {JWTTokenService} from '../../services/JWTTokenService';

@Component({
  selector: 'app-settings',
  templateUrl: './settings.component.html',
  styleUrls: ['./settings.component.scss']
})
export class SettingsComponent implements OnInit {

  constructor(private settings: SettingsService, private token: JWTTokenService) { }
  availableDevices: string[] = [
    'System default device',
    'Alternative device 2',
    'Alternative device 3'];

  micOn = false;

  cameraOn = true;

  message = 'Settings successfully saved!';

  displaySuccess = false;

  ngOnInit(): void {
    this.settings.getSettings(this.token.getUsername()).subscribe(
      next => {
        this.micOn = next.defaultMicOn;
        this.cameraOn = next.defaultCamOn;
      },
      err => { console.log(err); },
      () => {
      }
    );
  }

  onFileSelected(): void{
    // upload to s3
  }


  saveSettings(): void{
    console.log(this.micOn);
    this.settings.setSettings(this.token.getUsername(), this.micOn, this.cameraOn).subscribe(
      next => console.log(next),
      err => console.log(err),
      () => {

        this.displaySuccess = true;
      }
    );
  }
}
