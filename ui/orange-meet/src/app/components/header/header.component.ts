import {Component, OnInit} from '@angular/core';
import {JWTTokenService} from '../../services/JWTTokenService';
import {CookieService} from '../../services/CookieService';
import {Router} from '@angular/router';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss']
})
export class HeaderComponent implements OnInit {

  constructor(private jwtService: JWTTokenService, private cookieService: CookieService, private router: Router) { }
  isLoggedIn = !this.jwtService.isAccessTokenExpired();

  ngOnInit(): void {
    this.isLoggedIn = !this.jwtService.isAccessTokenExpired();
  }

  logout(): void{
    this.cookieService.remove('accessToken');
    this.cookieService.remove('refreshToken');
    this.router.navigateByUrl('/login');
  }
}
