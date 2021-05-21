import {Component, OnInit} from '@angular/core';
import {JWTTokenService} from '../../services/JWTTokenService';
import {Router} from '@angular/router';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent implements OnInit {

  constructor(private token: JWTTokenService, private router: Router) { }

  langs: Lang[] = [
    {value: 'en', viewValue: 'English'},
    {value: 'pl', viewValue: 'Polish'}
  ];

  selected = 'en';

  ngOnInit(): void {
    if (!this.token.isAccessTokenExpired()) {
      this.router.navigateByUrl('/login');
    }
  }

}

interface Lang {
  value: string;
  viewValue: string;
}
