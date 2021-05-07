import {Injectable} from '@angular/core';
import {HttpClient, HttpParams} from '@angular/common/http';
import {Observable} from 'rxjs';


@Injectable()
export class MeetingsService {

  constructor(private http: HttpClient) {}


  payload = 'payload';
  findMeetings(
    username: string,
    filter = '', sortOrder = true,
    pageNumber = 0, pageSize = 6): Observable<any> {

    return this.http.get(`http://130.61.186.61:9000/api/v1/meetings/owner/${username}/page`, {
      params: new HttpParams()
        .set('filter', filter)
        .set('meetingNameSortAscending', String(sortOrder))
        .set('page', pageNumber.toString())
        .set('size', pageSize.toString())
    });
    }

  createMeeting(username: string, name: string, participants: string[]): Observable<any> {
    return this.http.post(`http://130.61.186.61:9000/api/v1/meetings/owner/${username}/page`, {
     date: new Date(),
      name,
      ownerUserName: username,
      participants: JSON.stringify(participants)
    });
  }
}

