import {Component, OnInit} from '@angular/core';
import {SettingsService} from '../../services/backend.api/SettingsService';
import {JWTTokenService} from '../../services/auth/JWTTokenService';

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

  isPrivate = false;

  message = 'Settings successfully saved!';

  displaySuccess = false;

  image: any;

  ngOnInit(): void {
    this.settings.getSettings(this.token.getUsername()).subscribe(
      next => {
        this.micOn = next.defaultMicOn;
        this.cameraOn = next.defaultCamOn;
        this.isPrivate = next.private;
      },
      err => { console.log(err); },
      () => {
      }
    );
  }

  onFileSelected(event: any): void{
    this.image = event.target.files[0];
  }


  saveSettings(): void{
    console.log(this.micOn);
    this.settings.setSettings(this.token.getUsername(), this.micOn, this.cameraOn, this.isPrivate).subscribe(
      (next) => {},
      err => {},
      () => {
        if (this.image) {
          this.settings.uploadImage(this.token.getUsername(), this.image).subscribe(
            (next) => {
              console.log(next);
            },
            () => {
            },
            () => {
              this.displaySuccess = true;
            }
          );
        }
          else {
            this.displaySuccess = true;
          }
      }
    );
  }
}
