import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {Observable} from 'rxjs';

@Injectable()
export class LogoutService {

  constructor(private http: HttpClient) {
  }

  logout(username: string): Observable<any> {     // TODO questionable behavior not used

    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
      'Access-Control-Allow-Methods': 'POST',
      'Access-Control-Allow-Headers': 'Content-Type',
      'Access-Control-Allow-Origin': 'http://130.61.186.61:9000/', // TODO remove after replacing URL with relative one
      Anonymous: ''
    });

    return this.http.post(`http://backend:9000/api/v1/account/${username}/logout`, '', {headers});
  }
}
