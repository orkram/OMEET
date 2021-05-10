import {Component, OnInit} from '@angular/core';
import {Router} from '@angular/router';
import {JWTTokenService} from '../../services/JWTTokenService';

@Component({
  selector: 'app-meetings',
  templateUrl: './meetings.component.html',
  styleUrls: ['./meetings.component.scss']
})
export class MeetingsComponent implements OnInit {

  constructor(private router: Router, private jwtService: JWTTokenService) { }

  ngOnInit(): void {
    if (!this.jwtService.isAccessTokenExpired()){
      this.router.navigateByUrl('/meetings');
    }
  }

}
