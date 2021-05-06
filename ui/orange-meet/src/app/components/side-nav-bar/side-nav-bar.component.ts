import {Component, OnInit} from '@angular/core';
import {JWTTokenService} from '../../services/JWTTokenService';

@Component({
  selector: 'app-side-nav-bar',
  templateUrl: './side-nav-bar.component.html',
  styleUrls: ['./side-nav-bar.component.scss']
})
export class SideNavBarComponent implements OnInit {
  constructor(private jwtService: JWTTokenService) { }
  isLoggedIn = false;

  ngOnInit(): void {
    this.isLoggedIn = !this.jwtService.isAccessTokenExpired();

  }

}
