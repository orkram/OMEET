import {Component, ElementRef, OnInit, ViewChild} from '@angular/core';
import {JWTTokenService} from '../../../services/auth/JWTTokenService';
import {UserService} from '../../../services/backend.api/UserService';

@Component({
  selector: 'app-user-info',
  templateUrl: './user-info.component.html',
  styleUrls: ['./user-info.component.scss']
})
export class UserInfoComponent implements OnInit {

  @ViewChild('userImage', { static: true }) userImage!: ElementRef;

  constructor(private jwtToken: JWTTokenService, private userService: UserService) {
  }

  email = this.jwtToken.getEmail();
  username = this.jwtToken.getUsername();

  ngOnInit(): void {
    this.userService.getUserImage(this.jwtToken.getUsername()).subscribe(
      next => {
        console.log(next.url);
        this.userImage.nativeElement.src = next.url ;
      }
    );

  }

  errorHandler(event: any): void{
    event.target.src = '../../../../assets/images/small-user-image.png';
  }

}
