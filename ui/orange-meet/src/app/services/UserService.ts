import {Injectable} from '@angular/core';
import {HttpClient, HttpParams} from '@angular/common/http';
import {Observable} from 'rxjs';


@Injectable()
export class UserService {

  constructor(private http: HttpClient) {}

  payload = 'payload';
  findContacts(
    username: string,
    filter = '', sortOrder = true,
    pageNumber = 0, pageSize = 6): Observable<any> {

    return this.http.get(`backend:9000/api/v1/contacts/friends/${username}/page`, {
      params: new HttpParams()
        .set('query', filter)
        .set('lastNameSortAscending', 'false')
        .set('userNameSortAscending', String(sortOrder))
        .set('firstNameSortAscending', 'false')
        .set('page', pageNumber.toString())
        .set('size', pageSize.toString())
    });
  }

  findContactToAdd(filter: string): Observable<any> {
    return this.http.get(`backend:9000/api/v1/users/page`, {
      params: new HttpParams()
        .set('query', filter)
        .set('lastNameSortAscending', 'true')
        .set('userNameSortAscending', 'false')
        .set('firstNameSortAscending', 'false')
        .set('page', '1')
        .set('size', '5')
    });
  }

  createConnection(username: string, friendName: string): Observable<any> {
    return this.http.post(`backend:9000/api/v1/contacts/add`, '', {
      params: new HttpParams()
      .set('user-f', friendName)
      .set('user-o', username)
    });
  }
}

