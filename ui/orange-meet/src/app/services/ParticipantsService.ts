import {Injectable} from '@angular/core';
import {HttpClient, HttpParams} from '@angular/common/http';
import {Observable} from 'rxjs';


@Injectable()
export class ParticipantsService {

  constructor(private http: HttpClient) {}

  payload = 'payload';

  getParticipants(
    id: string): Observable<any> {

    return this.http.get(`http://backend:9000/api/v1/meetings/participants/meeting/${id}`, {
      params: new HttpParams()
        .set('query', '')
        .set('lastNameSortAscending', 'false')
        .set('userNameSortAscending', 'false')
        .set('firstNameSortAscending', 'true')
    });
  }
}

