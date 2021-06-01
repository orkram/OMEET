import {Component, OnInit} from '@angular/core';
import {FormControl, FormGroup} from '@angular/forms';
import {JWTTokenService} from '../../services/auth/JWTTokenService';
import {UserService} from '../../services/backend.api/UserService';
import {Observable} from 'rxjs';
import {map} from 'rxjs/operators';
import {flatMap} from 'rxjs/internal/operators';

@Component({
  selector: 'app-add-user',
  templateUrl: './add-user.component.html',
  styleUrls: ['./add-user.component.scss']
})
export class AddUserComponent implements OnInit {

  constructor(private userService: UserService, private jwtService: JWTTokenService) { }

  form: FormGroup = new FormGroup({
    name: new FormControl(null),
  });

  hide = true;

  errorMessage = false;

  myControl = new FormControl();

  filterUsers: Observable<Array<any>> =
    this.myControl.valueChanges.pipe(
      flatMap  (value => this.userService.findContactToAdd(value)
        .pipe(map((x: any) => x.foundUsers))
      ));

  responseText = 'User was added to your list';
  errorText = 'User not found';

  ngOnInit(): void {}

  submit(): void {
    if (this.form.invalid) {
      return;
    } else {
      this.userService.createConnection(this.jwtService.getUsername(), this.myControl.value.replace(/ *\([^)]*\) */g, ''))
        .subscribe(
          res => {
            console.log(res);
          },
          err => {
            console.log(err);
            this.hide = false;
            this.errorMessage = true;
          },
          () => {
            this.hide = true;
            this.errorMessage = false;
            window.location.reload();
          }
        );
    }
  }
}
