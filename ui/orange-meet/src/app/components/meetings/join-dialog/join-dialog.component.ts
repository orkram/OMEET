import {Component, OnInit} from '@angular/core';
import {FormControl, FormGroup, Validators} from '@angular/forms';
import {JWTTokenService} from '../../../services/JWTTokenService';

@Component({
  selector: 'app-join-dialog',
  templateUrl: './join-dialog.component.html',
  styleUrls: ['./join-dialog.component.scss']
})
export class JoinDialogComponent implements OnInit {

  constructor(private tokenService: JWTTokenService) { }

  form: FormGroup = new FormGroup({
    id: new FormControl('', Validators.compose([Validators.required])),
  });

  hide = true;

  submitted = false;

  errorMessage = false;

  ngOnInit(): void {
  }

  submit(): void{
    if (this.form.invalid) {
      return;
    } else {
      this.submitted = true;
      window.open(`http://backend/${this.form.value.id}#userInfo.displayName=%22${this.tokenService.getEmail()}%22&config.subject="Test meeting"`);
    }
  }

}

