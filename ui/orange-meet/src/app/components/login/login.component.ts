import {Component, OnInit} from '@angular/core';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent implements OnInit {

  constructor() { }

  langs: Lang[] = [
    {value: 'en', viewValue: 'English'},
    {value: 'es', viewValue: 'Spanish'},
    {value: 'pl', viewValue: 'Polish'}
  ];

  ngOnInit(): void {
  }

}

interface Lang {
  value: string;
  viewValue: string;
}
