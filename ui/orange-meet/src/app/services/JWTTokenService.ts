import {Injectable} from '@angular/core';
import jwtDecode from 'jwt-decode';
import {CookieService} from './CookieService';
import {Observable, throwError} from 'rxjs';
import {HttpClient, HttpHeaders} from '@angular/common/http';


@Injectable()
export class JWTTokenService {

  accessToken(): string {
    const t = this.cookieService.get('accessToken');
    if (t) {
      return jwtDecode(t);
    }
    return '';
  }

  refreshToken(): string {
    const t = this.cookieService.get('refreshToken');
    if (t) {
      return jwtDecode(t);
    }
    return '';
  }

  constructor(private cookieService: CookieService, private http: HttpClient) {
  }

  getEmail(): string {
    return JSON.parse(JSON.stringify(this.accessToken())).email;
  }

  getUsername(): string {
    return JSON.parse(JSON.stringify(this.accessToken())).preferred_username;
  }

  isAccessTokenExpired(): boolean {
    console.log(this.accessToken());
    if (this.accessToken()) {
      const expiryTime: number = +JSON.parse(JSON.stringify(this.accessToken())).exp;
      if (expiryTime) {
        console.log('Access token is expired');
        return ((1000 * expiryTime) - (new Date()).getTime()) < 5000;
      } else {
        return false;
      }
    } else {
      return true;
    }
  }

  isRefreshTokenExpired(): boolean {
    if (this.refreshToken()) {
      console.log(this.refreshToken());
      const expiryTime: number = +JSON.parse(JSON.stringify(this.refreshToken())).exp;
      if (expiryTime) {
        return ((1000 * expiryTime) - (new Date()).getTime()) < 4000;
      } else {
        return false;
      }
    } else {
      return true;
    }
  }

  getNewToken(): Observable<any> {
    if (!this.isRefreshTokenExpired()) {
      const headers = new HttpHeaders({
        'Content-Type': 'application/json',
        'Access-Control-Allow-Methods': 'POST',
        'Access-Control-Allow-Headers': 'Content-Type',
        'Access-Control-Allow-Origin': 'http://130.61.186.61:9000/', // TODO remove after replacing URL with relative one
         Anonymous: ''
      });
      return this.http.post(
        `backend:9000/api/v1/account/${this.getUsername()}/refresh-token`,
        {refreshToken: this.cookieService.get('refreshToken')}, {headers});
    } else {
      return throwError('Tokens expired');
    }
  }

 // hasActiveToken(): {
    // ???
  // }
}
