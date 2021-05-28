import {Component, OnInit} from '@angular/core';
import {FormControl, FormGroup, Validators} from '@angular/forms';
import {LoginService} from '../../../services/backend.api/LoginService';
import {Router} from '@angular/router';
import {CookieService} from '../../../services/auth/CookieService';
import {JWTTokenService} from '../../../services/auth/JWTTokenService';

@Component({
  selector: 'app-login-form',
  templateUrl: './login-form.component.html',
  styleUrls: ['./login-form.component.scss']
})
export class LoginFormComponent implements OnInit {

  constructor(
    private loginService: LoginService,
    private router: Router,
    private cookieService: CookieService,
    private jwtService: JWTTokenService) { }

  form: FormGroup = new FormGroup({
    username: new FormControl(null, [Validators.required]),
    password: new FormControl(null, Validators.compose([Validators.required])),
  });

  hide = true;

  submitted = false;

  errorMessage = false;

   loginRequestBody(): JSON {
     const body = this.form.getRawValue();
     body.clientId = 'orange-app';
     body.clientSecret = '881f08d9-7da9-4654-a3b2-8987b7a17506';
     return body;
   }

  ngOnInit(): void {
    if (!this.jwtService.isAccessTokenExpired()){
      this.router.navigateByUrl('/meetings');
    }
  }

  goToCreateAccount(): void {
    const navigationDetails: string[] = ['/registration'];
    this.router.navigate(navigationDetails);
  }

  submit(): void {
    if (this.form.invalid) {
      return;
    } else {
      this.submitted = true;
      this.loginService.login(this.form.value.username, this.form.value.password)
        .subscribe(
          res => {
            console.log(res);
            this.cookieService.set('accessToken', res.accessToken);
            this.cookieService.set('refreshToken', res.refreshToken);
          },
          err => {
            this.submitted = false;
            this.errorMessage = true;
          },
          () => {
            this.router.navigate(['meetings']);
          }
        );
    }
  }

}
