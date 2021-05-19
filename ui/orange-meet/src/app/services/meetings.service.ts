import {Injectable} from '@angular/core';
import {HttpClient, HttpParams} from '@angular/common/http';
import {Observable} from 'rxjs';
import {DatePipe} from '@angular/common';


@Injectable()
export class MeetingsService {

  constructor(private http: HttpClient, public datepipe: DatePipe) {}


  payload = 'payload';
  findMeetings(
    username: string,
    filter = '', sortOrder = true,
    pageNumber = 0, pageSize = 6): Observable<any> {

    return this.http.get(`http://130.61.186.61:9000/api/v1/meetings/owner/${username}/page`, {
      params: new HttpParams()
        .set('query', filter)
        .set('meetingNameSortAscending', 'false')
        .set('page', pageNumber.toString())
        .set('size', pageSize.toString())
    });
    }

  createMeeting(username: string, name: string, participants: string[]): Observable<any> {
    console.log(participants);
    return this.http.post(`http://130.61.186.61:9000/api/v1/meetings`, {
     date: this.datepipe.transform(new Date(), 'yyyy-MM-dd HH:mm:ss'),
      name,
      ownerUserName: username,
      participants
    });
  }
}

