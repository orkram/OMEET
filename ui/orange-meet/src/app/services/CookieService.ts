import {Injectable} from '@angular/core';

@Injectable({
  providedIn: 'root',
})
export class CookieService {
  private cookieStore = {};

  constructor() {
    this.parseCookies(document.cookie);
  }

  public parseCookies(cookies = document.cookie): void  {
    this.cookieStore = {};
    if (!cookies) { return; }
    const cookiesArr = cookies.split(';');
    for (const cookie of cookiesArr) {
      const cookieArr = cookie.split('=');
      // @ts-ignore
      this.cookieStore[cookieArr[0].trim()] = cookieArr[1]; // TODO
    }
  }

  get(key: string): string {
    this.parseCookies();
    // @ts-ignore
    return !!this.cookieStore[key] ? this.cookieStore[key] : null;
  }

  remove(key: string): void {
    document.cookie = `${key} = ; expires=Thu, 1 jan 1990 12:00:00 UTC; path=/`;
  }

  set(key: string, value: string): void {
    document.cookie = key + '=' + (value || '');
  }
}
