import {Injectable} from '@angular/core';
import {ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot} from '@angular/router';
import {Observable, of} from 'rxjs';
import {LoginService} from '../backend.api/LoginService';
import {CookieService} from './CookieService';
import {JWTTokenService} from './JWTTokenService';
import {catchError} from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class AuthorizeGuard implements CanActivate {
  constructor(private loginService: LoginService,
              private authStorageService: CookieService,
              private jwtService: JWTTokenService,
              private router: Router) {
  }
  canActivate(
    next: ActivatedRouteSnapshot,
    state: RouterStateSnapshot): Observable<any> | Promise<any> | boolean {
      if (this.jwtService.isAccessTokenExpired()) {
        this.jwtService.getNewToken()
          .pipe(
            catchError(_ => of([]))
          ).subscribe(
            res => {
              console.log('Token refreshed');
              this.authStorageService.set('accessToken', res.accessToken);
              this.authStorageService.set('refreshToken', res.refreshToken);
              window.location.reload();
            },
            _ => {
              console.log('Error while fetching new token');
              this.router.navigateByUrl('/login');
            },
            () => {}
        );
        return this.router.navigateByUrl(this.router.url);
      } else {
        return true;
      }
  }
}
