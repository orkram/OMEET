import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';


@Injectable()
export class SettingsService {

  constructor(private http: HttpClient) {}

  payload = 'payload';

  getSettings(
    username: string): Observable<any> {
    return this.http.get(`http://130.61.186.61:9000/api/v1/users/settings/${username}`);
  }

  requestBody(micOn: boolean,
              cameraOn: boolean): string {
   const r = JSON.stringify( {
      isDefaultCamOn: micOn,
      isDefaultMicOn: cameraOn
    });
   return JSON.parse(r);
}

  setSettings(
    username: string,
    micOn: boolean,
    cameraOn: boolean): Observable<any> {

    return this.http.put(`http://130.61.186.61:9000/api/v1/users/settings/${username}`, this.requestBody(micOn, cameraOn));
  }
}

