import {Component, OnInit} from '@angular/core';
import {JWTTokenService} from '../../services/auth/JWTTokenService';
import {Router} from '@angular/router';
import {TranslateService} from '@ngx-translate/core';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent implements OnInit {

  constructor(private token: JWTTokenService, private router: Router, public translate: TranslateService) {
    this.selected = this.translate.currentLang;
    console.log(this.translate.currentLang);
  }

  langs: Lang[] = [
    {value: 'en', viewValue: 'English'},
    {value: 'pl', viewValue: 'Polish'}
  ];

  selected = 'en';

  setLocale(): void{
    this.translate.setDefaultLang(this.selected);
    this.translate.use(this.selected);
    localStorage.setItem('appLanguage', this.selected);
  }

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
