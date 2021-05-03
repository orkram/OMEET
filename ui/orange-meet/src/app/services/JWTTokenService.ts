import {Injectable} from '@angular/core';
import jwtDecode from 'jwt-decode';


@Injectable()
export class JWTTokenService {

  get jwtToken(): string {
    return this._jwtToken;
  }
  set jwtToken(token: string) {
    if (token) {
      this._jwtToken = token;
    }
  }
  private _jwtToken: string;
  decodedToken: { [key: string]: string };

  constructor() {
  }

  decodeToken(): void {
    if (this._jwtToken) {
      this.decodedToken = jwtDecode(this._jwtToken);
    }
  }

  getDecodeToken(): void {
    return jwtDecode(this._jwtToken);
  }

  getEmailId(): string {
    this.decodeToken();
    return this.decodedToken ? this.decodedToken.email : null as any;
  }

  getExpiryTime(): number {
    this.decodeToken();
    return this.decodedToken ? this.decodedToken.exp : null as any;
  }

  isTokenExpired(): boolean {
    const expiryTime: number = this.getExpiryTime();
    if (expiryTime) {
      return ((1000 * expiryTime) - (new Date()).getTime()) < 5000;
    } else {
      return false;
    }
  }
}
