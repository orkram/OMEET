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

  micOn = false;

  cameraOn = true;

  isPrivate = false;

  displaySuccess = false;

  image: any;

  raiseError = false;


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
    const validImageTypes = ['image/jpeg', 'image/png', 'image/jpg'];
    console.log(event.target.files[0].size);
    if (validImageTypes.includes(event.target.files[0].type) && event.target.files[0].size < 5000000){
      this.raiseError = false;
      this.image = event.target.files[0];
      event.target.name = event.target.files[0].name;
      console.log(event.target.files[0].type);
    }
    else {
      this.raiseError = true;
      event.target.name = event.target.files[0].name;
    }
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
