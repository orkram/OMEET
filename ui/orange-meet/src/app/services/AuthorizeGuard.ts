import {Injectable} from '@angular/core';
import {ActivatedRouteSnapshot, CanActivate, RouterStateSnapshot} from '@angular/router';
import {Observable} from 'rxjs';
import {LoginService} from './LoginService';
import {CookieService} from './CookieService';

@Injectable({
  providedIn: 'root'
})
export class AuthorizeGuard implements CanActivate {
  constructor(private loginService: LoginService,
              private authStorageService: CookieService) {
  }
  canActivate(
    next: ActivatedRouteSnapshot,
    state: RouterStateSnapshot): Observable<any> | Promise<any> | boolean {
    if (this.jwtService.getUser()) {
      if (this.jwtService.isTokenExpired()) {
        // Try to get new access token using refresh token
        // if fail -> redirect to login page
      } else {
        return true;
      }
    } else {
      return new Promise((resolve) => {
        this.loginService.signIncallBack().then((e) => {
          resolve(true);
        }).catch((e) => {
          // Should Redirect Sign-In Page
        });
      });
    }
  }
}
