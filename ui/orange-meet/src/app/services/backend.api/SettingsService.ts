import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {Observable} from 'rxjs';
import {mergeMap} from 'rxjs/operators';


@Injectable()
export class SettingsService {

  constructor(private http: HttpClient) {}

  payload = 'payload';

  getSettings(
    username: string): Observable<any> {
    return this.http.get(`http://130.61.186.61:9000/api/v1/users/settings/${username}`);
  }

  requestBody(micOn: boolean,
              cameraOn: boolean,
              isPrivate: boolean): string {
   const r = JSON.stringify( {
      isDefaultCamOn: cameraOn,
      isDefaultMicOn: micOn,
      private: isPrivate
    });
   return JSON.parse(r);
}

  uploadImage(username: string, image: any): Observable<any>{
     return this.http.get(`http://130.61.186.61:9000/api/v1/users/${username}/avatar/update`).pipe(
       mergeMap ((response: any) => {
         console.log(response);

         const headers =  new HttpHeaders(
           {
             Anonymous: '',
             'Content-Type': 'multipart/form-data'
           }
         );

         return this.http.put(
           response.imgUpdateUrl,
           image,
           {headers}
           );
       })
     );
  }

  setSettings(
    username: string,
    micOn: boolean,
    cameraOn: boolean,
    isPrivate: boolean): Observable<any> {
    return this.http.put(`http://130.61.186.61:9000/api/v1/users/settings/${username}`, this.requestBody(micOn, cameraOn, isPrivate));
  }
}
