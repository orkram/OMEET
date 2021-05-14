import {Component, OnInit} from '@angular/core';
import {CookieService} from '../../services/CookieService';
import {Router} from '@angular/router';
import {JWTTokenService} from '../../services/JWTTokenService';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss']
})
export class HeaderComponent implements OnInit {

  constructor( private cookieService: CookieService, private router: Router, private jwtToken: JWTTokenService) { }

  logoutVisible = false;

  email = this.jwtToken.getEmail();
  username = this.jwtToken.getUsername();

  ngOnInit(): void {
  }

  changeVisibility(): void{
    this.logoutVisible = !this.logoutVisible;
  }


  logout(): void{
    this.cookieService.remove('accessToken');
    this.cookieService.remove('refreshToken');
    this.router.navigateByUrl('/login');
  }
}
