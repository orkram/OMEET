import {AfterViewInit, Component, OnInit} from '@angular/core';
import {JWTTokenService} from '../services/auth/JWTTokenService';
import {Router} from '@angular/router';
import {SettingsService} from '../services/backend.api/SettingsService';
import {UserService} from '../services/backend.api/UserService';

declare var JitsiMeetExternalAPI: any;

@Component({
  selector: 'app-jitsi-view',
  templateUrl: './jitsi-view.component.html',
  styleUrls: ['./jitsi-view.component.scss']
})
export class JitsiViewComponent implements OnInit, AfterViewInit {

  domain = '130.61.186.61';
  room: any;
  options: any;
  api: any;
  user: any;
  img: any;
  isAudioMuted = false;
  isVideoMuted = false;

  constructor(
    private router: Router,
    private token: JWTTokenService,
    private settings: SettingsService,
    private userService: UserService
  ) {
  }

  ngOnInit(): void {

    const url = this.router.url.split('/');
    this.room =  url[url.length - 1];

    this.user = {
      name: this.token.getUsername()
    };

    this.userService.getUserImage(this.token.getUsername()).subscribe(res => this.img = res.imgURL);
  }

  ngAfterViewInit(): void {
    this.options = {
      roomName: this.room,
      height: window.innerHeight,
      configOverwrite: {prejoinPageEnabled: false},
      interfaceConfigOverwrite: {
      },
      parentNode: document.querySelector('#jitsi-iframe'),
      userInfo: {
        displayName: this.user.name
      }
    };

    this.api = new JitsiMeetExternalAPI(this.domain, this.options);
// Event handlers
    this.api.addEventListeners({
      readyToClose: this.handleClose,
      participantLeft: this.handleParticipantLeft,
      participantJoined: this.handleParticipantJoined,
      videoConferenceJoined: this.handleVideoConferenceJoined,
      videoConferenceLeft: this.handleVideoConferenceLeft,
      audioMuteStatusChanged: this.handleMuteStatus,
      videoMuteStatusChanged: this.handleVideoStatus
    });


    this.settings.getSettings(this.token.getUsername()).subscribe(
      (next) => {

        if (this.img){
          console.log('Image set to: ' + this.img);
          this.api.executeCommand('avatarUrl', this.img);
        }

        if (!next.defaultMicOn){
         this.api.executeCommand('toggleAudio');
       }

        if (!next.defaultCamOn){
          this.api.executeCommand('toggleVideo');
        }
      },
      err => {},
      () => {}
    );
  }

  handleClose = () => {
    console.log('handleClose');
  }

  handleParticipantLeft = async (participant: any) => {
    console.log('handleParticipantLeft', participant);
    const data = await this.getParticipants();
  }

  handleParticipantJoined = async (participant: any) => {
    console.log('handleParticipantJoined', participant);
    const data = await this.getParticipants();
  }

  handleVideoConferenceJoined = async (participant: any) => {
    console.log('handleVideoConferenceJoined', participant);
    const data = await this.getParticipants();
  }

  handleVideoConferenceLeft = () => {
    console.log('handleVideoConferenceLeft');
    this.router.navigate(['/meetings']);
  }

  handleMuteStatus = (audio: any) => {
  }
  handleVideoStatus = (video: any) => {
  }

  getParticipants(): Promise<any> {
    return new Promise((resolve, reject) => {
      setTimeout(() => {
        resolve(this.api.getParticipantsInfo());
      }, 500);
    });
  }

  executeCommand(command: string): void {
    this.api.executeCommand(command);
    if (command === 'hangup') {
      this.router.navigate(['/meetings']);
      return;
    }

    if (command === 'toggleAudio') {
      this.isAudioMuted = !this.isAudioMuted;
    }

    if (command === 'toggleVideo') {
      this.isVideoMuted = !this.isVideoMuted;
    }
  }


}
