import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {Observable} from 'rxjs';
import {FormGroup} from '@angular/forms';

@Injectable()
export class LoginService {

  constructor(private http: HttpClient) {
  }

  private static loginRequestBody(username: string, password: string): string {
    const body = JSON.stringify({
      username,
      password
    });
    return JSON.parse(body);
  }

  private registerRequestBody(form: FormGroup): string {
    const inputs = form.value;
    const body = JSON.stringify(
      {
        userName: inputs.username,
        password: inputs.password,
        eMail: inputs.email,
        firstName: inputs.firstname,
        lastName: inputs.lastname,
        imgUrl: ''  // TODO make optional in backend
      }
    );
    console.log(body);
    return JSON.parse(body);
  }

  login(username: string, password: string): Observable<any> {

    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
      'Access-Control-Allow-Methods': 'POST',
      'Access-Control-Allow-Headers': 'Content-Type',
      'Access-Control-Allow-Origin': 'backend:9000/', // TODO remove after replacing URL with relative one
       Anonymous: ''
    });

    return this.http.post('backend:9000/api/v1/account/login', LoginService.loginRequestBody(username, password), {headers});
  }

  registerUser(form: FormGroup): Observable<any> {
    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
      'Access-Control-Allow-Methods': 'POST',
      'Access-Control-Allow-Headers': 'Content-Type',
      'Access-Control-Allow-Origin': 'backend:9000/', // TODO remove after replacing URL with relative one
      Anonymous: ''
    });

    return this.http.post('backend:9000/api/v1/account/register', this.registerRequestBody(form), {headers});
  }
}
