import {Component, OnInit, ViewChild} from '@angular/core';
import {BehaviorSubject, Observable, of} from 'rxjs';
import {CollectionViewer, DataSource} from '@angular/cdk/collections';
import {animate, state, style, transition, trigger} from '@angular/animations';
import {MatPaginator} from "@angular/material/paginator";
import {catchError, finalize} from "rxjs/operators";

@Component({
  selector: 'app-meetings-list',
  templateUrl: './meetings-list.component.html',
  styleUrls: ['./meetings-list.component.scss'],
  animations: [
    trigger('detailExpand', [
      state('collapsed', style({ height: '0px', minHeight: '0', visibility: 'hidden' })),
      state('expanded', style({ height: '*', visibility: 'visible' })),
      transition('expanded <=> collapsed', animate('225ms cubic-bezier(0.4, 0.0, 0.2, 1)')),
    ]),
  ]
})
export class MeetingsListComponent implements OnInit {

  constructor() { }

  expandedMeeting: any;

  displayedColumns = ['id', 'name', 'status', 'date'];
  dataSource = new ExampleDataSource();

  @ViewChild(MatPaginator, {static: true})
  paginator!: MatPaginator;

  ngOnInit(): void {
    this.dataSource.paginator = this.paginator;
  }

  // tslint:disable-next-line:typedef
  applyFilter(key: string){
    console.log(key);
  }


  isExpansionDetailRow = (i: number, row: object) => row.hasOwnProperty('detailRow');

}

export enum Status{
  Active = 'Active',
  Inactive = 'Inactive'
}
export class Meeting {
  id: string;
  name: string;
  status: Status = Status.Inactive;
  listOfParticipants: string[] = [];
  description = 'Short meeting description(Optional)';
  date = new Date().toISOString();

  constructor(id: string, name: string, status: Status) {
    this.id = id;
    this.name = name;
    this.status = status;
  }
}

const data: Meeting[] = [
  new Meeting('1', 'Test meeting 1', Status.Active),
  new Meeting('2', 'Test meeting 2', Status.Active),

  new Meeting('3', 'Test meeting 3', Status.Active),

  new Meeting('4', 'Test meeting 4', Status.Active),

  new Meeting('5', 'Test meeting 5', Status.Active),

  new Meeting('6', 'Test meeting 6', Status.Active),

  new Meeting('7', 'Test meeting 7', Status.Active),

];

export class ExampleDataSource extends DataSource<any> {

  private lessonsSubject = new BehaviorSubject<Meeting[]>([]);
  private loadingSubject = new BehaviorSubject<boolean>(false);

  public loading$ = this.loadingSubject.asObservable();

  connect(): Observable<Meeting[]> {
    const rows: any[] = [];
    data.forEach(meeting => rows.push(meeting, { detailRow: true, meeting }));
    console.log(rows);
    return of(rows);
  }
  length(): number{
    return data.length;
  }

  disconnect(collectionViewer: CollectionViewer): void {
  }

  // tslint:disable-next-line:typedef
  loadLessons(courseId: number, filter = '',
              sortDirection = 'asc', pageIndex = 0, pageSize = 3) {

    this.loadingSubject.next(true);

    this.subscribe(lessons => this.lessonsSubject.next(lessons));
  }
}
