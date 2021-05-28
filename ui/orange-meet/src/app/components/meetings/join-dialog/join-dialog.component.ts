import {Component, OnInit} from '@angular/core';
import {FormControl, FormGroup, Validators} from '@angular/forms';
import {JWTTokenService} from '../../../services/auth/JWTTokenService';
import {Router} from '@angular/router';
import {MatDialogRef} from '@angular/material/dialog';

@Component({
  selector: 'app-join-dialog',
  templateUrl: './join-dialog.component.html',
  styleUrls: ['./join-dialog.component.scss']
})
export class JoinDialogComponent implements OnInit {

  constructor(private tokenService: JWTTokenService, private router: Router,
              private dialogRef: MatDialogRef<JoinDialogComponent>
  ) { }

  form: FormGroup = new FormGroup({
    id: new FormControl('', Validators.compose([Validators.required])),
  });

  hide = true;

  submitted = false;

  errorMessage = false;

  ngOnInit(): void {
  }

  submit(): void{
    this.router.navigateByUrl(`/meeting/${this.form.value.id}`);
    this.dialogRef.close();
  }

}

