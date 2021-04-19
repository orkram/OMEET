import {Injectable} from '@angular/core';
import {HttpClient, HttpParams} from '@angular/common/http';
import {Observable, of} from 'rxjs';
import {Meeting, Status} from '../model/meeting';


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
    // return response.pipe( map(res =>  res[this.payload] ));

    let rows: any[];
    rows = [];
    data.forEach(meeting => rows.push(meeting, { detailRow: true, meeting }));
    return of(rows);
  }
}

const data: Meeting[] = [
  new Meeting('1', 'Test meeting 1', Status.Active),
  new Meeting('2', 'Test meeting 2', Status.Active),

  new Meeting('3', 'Test meeting 3', Status.Active),
];
