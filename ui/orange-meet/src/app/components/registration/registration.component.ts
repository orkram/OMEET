import {Component, OnInit} from '@angular/core';

@Component({
  selector: 'app-registration',
  templateUrl: './registration.component.html',
  styleUrls: ['./registration.component.scss']
})
export class RegistrationComponent implements OnInit {

  constructor() { }
  langs: Lang[] = [
    {value: 'en', viewValue: 'English'},
    {value: 'pl', viewValue: 'Polish'}
  ];

  selected = 'English';
  ngOnInit(): void {
  }

}
interface Lang {
  value: string;
  viewValue: string;
}
