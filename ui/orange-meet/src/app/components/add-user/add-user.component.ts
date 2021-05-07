import {Component, OnInit} from '@angular/core';
import {FormControl, FormGroup} from '@angular/forms';
import {JWTTokenService} from '../../services/JWTTokenService';
import {UserService} from '../../services/UserService';

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

  submitted = false;

  errorMessage = false;

  ngOnInit(): void {
  }

  submit(): void {
    if (this.form.invalid) {
      return;
    } else {
      this.submitted = true;
      this.userService.createConnection(this.jwtService.getUsername(), this.form.value.name)
        .subscribe(
          res => {
            console.log(res);
          },
          err => {
            this.submitted = false;
            this.errorMessage = true;
          },
          () => {}// TODO Refresh list?
        );
    }
  }

}
