import {HttpEvent, HttpHandler, HttpInterceptor, HttpRequest} from '@angular/common/http';
import {Injectable} from '@angular/core';
import {CookieService} from './auth/CookieService';
import {Observable} from 'rxjs';

@Injectable()
export class AppInterceptor implements HttpInterceptor {

  constructor( private authService: CookieService) { }

  public intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    const token = this.authService.get('accessToken');
    if (req.headers.get('Anonymous') == null ) {
      if (token !== null) {
        req = req.clone({
          url: req.url,
          setHeaders: {
            Authorization: `Bearer ${token}`
          }
        });
      }
    }
    return next.handle(req);
  }
}
