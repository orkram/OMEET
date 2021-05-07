import {Component, OnInit} from '@angular/core';
import {CookieService} from '../../services/CookieService';
import {Router} from '@angular/router';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss']
})
export class HeaderComponent implements OnInit {

  constructor( private cookieService: CookieService, private router: Router) { }
  ngOnInit(): void {
  }

  logout(): void{
    this.cookieService.remove('accessToken');
    this.cookieService.remove('refreshToken');
    this.router.navigateByUrl('/login');
  }
}
