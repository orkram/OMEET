import {Component, Input, OnInit} from '@angular/core';
import {AbstractControl, FormControl, FormGroup, ValidationErrors, Validators} from '@angular/forms';
import {LoginService} from '../../../services/backend.api/LoginService';
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
    email: new FormControl('', Validators.compose([Validators.required, Validators.email, Validators.pattern('^[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,4}$')])),
    password: new FormControl('', Validators.compose([Validators.required, Validators.minLength(8)])),
    repeat: new FormControl('', [Validators.required, this.matchValues('password')]),
    firstname: new FormControl('', Validators.compose([Validators.required])),
    lastname: new FormControl('', Validators.compose([Validators.required]))
  });

  hide = true;

  submitted = false;

  errorMessage = false;

  successMessage = false;

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
          () => {
            this.form.reset();
            this.form.get('username')?.clearValidators();
            this.form.get('username')?.updateValueAndValidity();

            this.form.get('email')?.clearValidators();
            this.form.get('email')?.updateValueAndValidity();

            this.form.get('password')?.clearValidators();
            this.form.get('password')?.updateValueAndValidity();

            this.form.get('repeat')?.clearValidators();
            this.form.get('repeat')?.updateValueAndValidity();

            this.form.get('firstname')?.clearValidators();
            this.form.get('firstname')?.updateValueAndValidity();

            this.form.get('lastname')?.clearValidators();
            this.form.get('lastname')?.updateValueAndValidity();

            this.successMessage = true;
            // this.router.navigateByUrl('/login');
          }
        );
    }
  }

}
