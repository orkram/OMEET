import {Injectable} from '@angular/core';
import {HttpClient, HttpErrorResponse, HttpHeaders} from '@angular/common/http';
import {Observable, throwError} from 'rxjs';
import {FormGroup} from '@angular/forms';

@Injectable()
export class LoginService {

  constructor(private http: HttpClient) {
  }

  private static handleError(error: HttpErrorResponse): Observable<any> {
    if (error.status === 0) {
      // A client-side or network error occurred. Handle it accordingly.
      console.error('An error occurred:', error.error);
    } else {
      // The backend returned an unsuccessful response code.
      // The response body may contain clues as to what went wrong.
      console.error(
        `Backend returned code ${error.status}, ` +
        `body was: ${error.error}`);
    }
    // Return an observable with a user-facing error message.
    return throwError(
      'Something bad happened; please try again later.');
  }

  private loginRequestBody(username: string, password: string): string  {
    const body = JSON.stringify({username, password, clientId: 'orange-app', clientSecret: '881f08d9-7da9-4654-a3b2-8987b7a17506'});
    return JSON.parse(body);
  }
  private registerRequestBody(form: FormGroup): string {
    const inputs = form.value;
    const body = JSON.stringify(
      {
        username: inputs.username,
        password: inputs.password,
        eMail: inputs.email,
        firstName: inputs.firstname,
        lastName: inputs.lastname,
        imgUrl: ''  // TODO make optional in backend
      }
    );
    return JSON.parse(body);
  }

  login(username: string, password: string): Observable<any> {

    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
      'Access-Control-Allow-Methods': 'POST',
      'Access-Control-Allow-Headers': 'Content-Type',
      'Access-Control-Allow-Origin': 'http://130.61.186.61:9000/' // TODO remove after replacing URL with relative one
    });

    return this.http.post('http://130.61.186.61:9000/api/v1/account/login', this.loginRequestBody(username, password), {headers});
  }

  registerUser(form: FormGroup): Observable<any>{
    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
      'Access-Control-Allow-Methods': 'POST',
      'Access-Control-Allow-Headers': 'Content-Type',
      'Access-Control-Allow-Origin': 'http://130.61.186.61:9000/' // TODO remove after replacing URL with relative one
    });

    return this.http.post('http://130.61.186.61:9000/api/v1/account/register', this.registerRequestBody(form), {headers});
  }
}
