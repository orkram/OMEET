import {Component, OnInit} from '@angular/core';
import {JWTTokenService} from '../../../services/auth/JWTTokenService';

@Component({
  selector: 'app-user-info',
  templateUrl: './user-info.component.html',
  styleUrls: ['./user-info.component.scss']
})
export class UserInfoComponent implements OnInit {

  constructor(private jwtToken: JWTTokenService) { }

  email = this.jwtToken.getEmail();
  username = this.jwtToken.getUsername();

  ngOnInit(): void {
  }

}
