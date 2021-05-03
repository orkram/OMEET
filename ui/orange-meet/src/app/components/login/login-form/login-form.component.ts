import {Component, Input, OnInit} from '@angular/core';
import {FormControl, FormGroup} from '@angular/forms';
import {LoginService} from '../../../services/LoginService';
import {Router} from '@angular/router';

@Component({
  selector: 'app-login-form',
  templateUrl: './login-form.component.html',
  styleUrls: ['./login-form.component.scss']
})
export class LoginFormComponent implements OnInit {

  constructor(private loginService: LoginService,  private router: Router) { }

  form: FormGroup = new FormGroup({
    username: new FormControl(''),
    password: new FormControl(''),
  });
  hide = true;

  @Input() error!: string;
   loginRequestBody(): JSON {
     const body = this.form.getRawValue();
     body.clientId = 'orange-app';
     body.clientSecret = '881f08d9-7da9-4654-a3b2-8987b7a17506';
     return body;
   }

  ngOnInit(): void {
  }

  goToCreateAccount(): void {
    const navigationDetails: string[] = ['/registration'];
    this.router.navigate(navigationDetails);
  }

  submit(): void {
    this.loginService.login(this.form.value.username, this.form.value.password)
      .subscribe(
        () => { // parse messag
          this.router.navigateByUrl('/meetings');
        }
      );
  }

}
