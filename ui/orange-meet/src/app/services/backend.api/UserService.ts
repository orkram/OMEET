import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders, HttpParams} from '@angular/common/http';
import {Observable} from 'rxjs';


@Injectable()
export class UserService {

  constructor(private http: HttpClient) {
  }

  payload = 'payload';

  findContacts(
    username: string,
    filter = '', sortOrder = true,
    pageNumber = 0, pageSize = 6): Observable<any> {

    return this.http.get(`http://130.61.186.61:9000/api/v1/contacts/friends/${username}/page`, {
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
    return this.http.get(`http://130.61.186.61:9000/api/v1/users/page`, {
      params: new HttpParams()
        .set('query', filter)
        .set('lastNameSortAscending', 'true')
        .set('userNameSortAscending', 'false')
        .set('firstNameSortAscending', 'false')
        .set('page', '1')
        .set('size', '5')
    });
  }

  findAllContacts(username: string, filter: string): Observable<any> {
    return this.http.get(`http://130.61.186.61:9000/api/v1/contacts/friends/${username}`, {
      params: new HttpParams()
        .set('query', filter)
        .set('lastNameSortAscending', 'true')
        .set('userNameSortAscending', 'false')
        .set('firstNameSortAscending', 'false')
    });
  }

  getUserImage(username: string): Observable<any>{
    return this.http.get(`http://130.61.186.61:9000/api/v1/users/${username}`);
  }

  createConnection(username: string, friendName: string): Observable<any> {
    return this.http.get(`http://130.61.186.61:9000/api/v1/contacts/add`, {
      headers: new HttpHeaders({
         Accept: 'text/html, application/xhtml+xml, */*',
        'Content-Type': 'application/x-www-form-urlencoded'
      }),
        responseType: 'text',
      params: new HttpParams()
        .set('user-f', friendName)
        .set('user-o', username)
    },
      );
  }

  private readFile(blob: Blob): Observable<string> {
    return new Observable(obs => {
      const reader = new FileReader();

      reader.onerror = err => obs.error(err);
      reader.onabort = err => obs.error(err);
      reader.onload = () => obs.next(reader.result?.toString());
      reader.onloadend = () => obs.complete();

      return reader.readAsDataURL(blob);
    });
  }

  removeConnection(username: string, friend: string): Observable<any> {
    return this.http.delete(`http://130.61.186.61:9000/api/v1/contacts/friends/${username}`, {
        params: new HttpParams()
          .set('friend', friend)
      }
    );
  }
}
