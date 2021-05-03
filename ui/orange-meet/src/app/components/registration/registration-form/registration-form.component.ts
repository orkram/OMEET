import {Component, Input, OnInit} from '@angular/core';
import {FormControl, FormGroup} from '@angular/forms';
import {LoginService} from '../../../services/LoginService';
import {Router} from '@angular/router';

@Component({
  selector: 'app-registration-form',
  templateUrl: './registration-form.component.html',
  styleUrls: ['./registration-form.component.scss']
})
export class RegistrationFormComponent implements OnInit {

  constructor(private loginService: LoginService,  private router: Router) { }

  form: FormGroup = new FormGroup({
    username: new FormControl(''),
    email: new FormControl(''),
    password: new FormControl(''),
    repeat: new FormControl(''),
    firstname: new FormControl(''),
    lastname: new FormControl('')
  });
  hide = true;

  @Input() error!: string;

  ngOnInit(): void {
  }

  submit(): void {
    this.loginService.registerUser(this.form)
      .subscribe(
        () => { // parse messag
          this.router.navigateByUrl('/login');
        }
      );
  }

}
