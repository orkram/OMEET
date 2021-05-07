import {CollectionViewer, DataSource} from '@angular/cdk/collections';
import {BehaviorSubject, Observable, of} from 'rxjs';
import {catchError, finalize} from 'rxjs/operators';
import {MeetingsService} from './meetings.service';
import {Meeting} from '../model/meeting';


export class MeetingsDataSource extends DataSource<Meeting> {

  private meetingsSubject = new BehaviorSubject<Meeting[]>([]);
  private loadingSubject = new BehaviorSubject<boolean>(false);

  public loading$ = this.loadingSubject.asObservable();

  constructor(private meetingService: MeetingsService) {
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
                sortDirection = true, pageIndex = 0, pageSize = 6): void {

    this.loadingSubject.next(true);

    this.meetingService.findMeetings(username, filter, sortDirection,
      pageIndex, pageSize).pipe(
      catchError(() => of([])),
      finalize(() => this.loadingSubject.next(false))
    )
      .subscribe(meeting => {
        console.log(meeting);
        this.meetingsSubject.next(meeting.foundMeetings);
      });
  }
}


