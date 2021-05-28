import {AfterViewInit, Component, OnInit} from '@angular/core';
import {JWTTokenService} from '../services/auth/JWTTokenService';
import {Router} from '@angular/router';
import {SettingsService} from '../services/backend.api/SettingsService';

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

  isAudioMuted = false;
  isVideoMuted = false;

  constructor(
    private router: Router,
    private token: JWTTokenService,
    private settings: SettingsService
  ) {
  }

  ngOnInit(): void {

    console.log( this.router.url);

    const url = this.router.url.split('/');
    this.room =  url[url.length - 1];

    if (!this.token.isAccessTokenExpired()) {
      this.router.navigateByUrl(`/meeting/${this.room}`);
    }

    this.user = {
      name: this.token.getUsername()
    };

    this.settings.getSettings(this.token.getUsername()).subscribe(
      (next) => {
        this.isAudioMuted = !next.defaultMicOn;
        this.isVideoMuted = next.defaultCamOn;
      },
      err => {},
      () => {}
    );
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

    console.log(this.api);

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
    console.log('handleMuteStatus', audio);
  }
  handleVideoStatus = (video: any) => {
    console.log('handleVideoStatus', video);
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
