import {CollectionViewer, DataSource} from '@angular/cdk/collections';
import {BehaviorSubject, Observable, of} from 'rxjs';
import {catchError, finalize} from 'rxjs/operators';
import {MeetingsService} from './meetings.service';
import {Meeting, Status} from '../model/meeting';


export class MeetingsDataSource extends DataSource<Meeting> {

  private meetingsSubject = new BehaviorSubject<Meeting[]>([]);
  private loadingSubject = new BehaviorSubject<boolean>(false);

  public loading$ = this.loadingSubject.asObservable();

  constructor(private meetingService: MeetingsService) {
    super();
  }

  data(page: number): Meeting[]{
    return [new Meeting('1', `Test meeting ${page }`, Status.Active),
      new Meeting('2', `Test meeting ${2 * page }`, Status.Active),
      new Meeting('3', `Test meeting ${3 * page} `, Status.Active),
    ];
  }

  connect(): Observable<Meeting[]> {
    let rows: any[];
    rows = [];
    this.data(1).forEach(meeting => rows.push(meeting, { detailRow: true, meeting }));
    // return of(rows);
    return this.meetingsSubject.asObservable();
  }

  disconnect(collectionViewer: CollectionViewer): void {
    this.meetingsSubject.complete();
    this.loadingSubject.complete();
  }

  loadMeetings( filter = '',
                sortDirection = 'asc', pageIndex = 0, pageSize = 6): void {

    this.loadingSubject.next(true);

    this.meetingService.findMeetings(filter, sortDirection,
      pageIndex, pageSize).pipe(
      catchError(() => of([])),
      finalize(() => this.loadingSubject.next(false))
    )
      .subscribe(meeting => this.meetingsSubject.next(meeting));
  }
}


