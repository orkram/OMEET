import {Component, Input, OnInit} from '@angular/core';
import {AbstractControl, FormControl, FormGroup, ValidationErrors, Validators} from '@angular/forms';
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
    username: new FormControl('', Validators.compose([Validators.required])),
    email: new FormControl('', Validators.compose([Validators.required, Validators.email])),
    password: new FormControl('', Validators.compose([Validators.required, Validators.minLength(8)])),
    repeat: new FormControl('', [Validators.required, this.matchValues('password')]),
    firstname: new FormControl('', Validators.compose([Validators.required])),
    lastname: new FormControl('', Validators.compose([Validators.required]))
  });

  hide = true;

  submitted = false;

  errorMessage = false;

  @Input() error!: string;

  ngOnInit(): void {
  }

  matchValues(
    matchTo: string
  ): (_: AbstractControl) => ValidationErrors | null {
    return (control: AbstractControl): ValidationErrors | null => {
      return !!control.parent &&
      !!control.parent.value &&
      // @ts-ignore
      control.value === control.parent.controls[matchTo].value
        ? null
        : { isMatching: false };
    };
  }

  backToLogin(): void{
    this.router.navigateByUrl('/login');
  }

  submit(): void {
    if (this.form.invalid) {
      return;
    } else {
      this.loginService.registerUser(this.form)
        .subscribe(
          res => { console.log(res); },
            _ => {
              this.submitted = false;
              this.errorMessage = true;
            },
          () => { // parse messag
            this.router.navigateByUrl('/login');
          }
        );
    }
  }

}
