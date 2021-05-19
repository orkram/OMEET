import {CollectionViewer, DataSource} from '@angular/cdk/collections';
import {BehaviorSubject, Observable, of, Subscription} from 'rxjs';
import {catchError, finalize} from 'rxjs/operators';
import {MeetingsService} from './meetings.service';
import {Meeting} from '../model/meeting';
import {ParticipantsService} from './ParticipantsService';


export class MeetingsDataSource extends DataSource<Meeting> {

  private meetingsSubject = new BehaviorSubject<any>([]);
  private loadingSubject = new BehaviorSubject<boolean>(false);

  public length = 0;

  public loading$ = this.loadingSubject.asObservable();

  constructor(private meetingService: MeetingsService, private participantsService: ParticipantsService) {
    super();
  }

  connect(): Observable<Meeting[]> {
    return this.meetingsSubject.asObservable();
  }

  disconnect(collectionViewer: CollectionViewer): void {
    this.meetingsSubject.complete();
    this.loadingSubject.complete();
  }

  loadMeetings( username: string, filter = '',
                sortDirection = true, pageIndex = 0, pageSize = 6): Subscription {

    this.loadingSubject.next(true);

    return this.meetingService.findMeetings(username, filter, sortDirection,
      pageIndex, pageSize).pipe(
      catchError(() => of([])),
      finalize(() => this.loadingSubject.next(false))
    ).subscribe(meetings => {
      console.log(meetings.foundMeetings);
      const rows: any[] = [];
      this.length = meetings.allFoundUsers;
      console.log(this.length );
      meetings.foundMeetings.forEach( (meeting: any) => {
        this.participantsService.getParticipants(meeting.idMeeting).subscribe(
          next => {
            meeting.participants = next.map((participant: any) => participant.firstName + ' ' + participant.lastName);
          }
        );
        rows.push(meeting, { detailRow: true, meeting });
      });
      this.meetingsSubject.next(rows);
      });

  }
}


