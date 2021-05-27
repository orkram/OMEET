import {Injectable} from '@angular/core';
import {ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot} from '@angular/router';
import {Observable, of} from 'rxjs';
import {LoginService} from './LoginService';
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
        const k = this.jwtService.getNewToken()
          .pipe(
            catchError(_ => of([]))
          ).subscribe(
            res => {
              this.authStorageService.set('accessToken', res.accessToken);
              this.authStorageService.set('refreshToken', res.refreshToken);
            },
            _ => {
              console.log('Error while fetching new token');
              window.location.reload();
              this.router.navigateByUrl('/login');
              return false;
            },
            () => {
              return true;
            }
        );
        this.router.navigateByUrl('/login');
        return !!k;      // TODO wasn't tested
      } else {
        return true;
      }
  }
}