import {Injectable} from '@angular/core';
import {HttpClient, HttpParams} from '@angular/common/http';
import {Observable} from 'rxjs';
import {Meeting} from '../model/meeting';


@Injectable()
export class MeetingsService {

  constructor(private http: HttpClient) {}

  payload = 'payload';
  findMeetings(
    filter = '', sortOrder = 'asc',
    pageNumber = 0, pageSize = 6): Observable<Meeting[]> {

    const response = this.http.get('/api/meetings', {
      params: new HttpParams()
        .set('filter', filter)
        .set('sortOrder', sortOrder)
        .set('pageNumber', pageNumber.toString())
        .set('pageSize', pageSize.toString())
    });
    // @ts-ignore
    return response;
  }
}

