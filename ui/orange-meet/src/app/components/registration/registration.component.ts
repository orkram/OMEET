import {Component, OnInit} from '@angular/core';
import {Router} from '@angular/router';

@Component({
  selector: 'app-registration',
  templateUrl: './registration.component.html',
  styleUrls: ['./registration.component.scss']
})
export class RegistrationComponent implements OnInit {

  constructor(private router: Router) { }
  langs: Lang[] = [
    {value: 'en', viewValue: 'English'},
    {value: 'pl', viewValue: 'Polish'}
  ];

  selected = 'English';
  ngOnInit(): void {
  }

  backToLogin(): void{
    this.router.navigateByUrl('/login');
  }

}
interface Lang {
  value: string;
  viewValue: string;
}
