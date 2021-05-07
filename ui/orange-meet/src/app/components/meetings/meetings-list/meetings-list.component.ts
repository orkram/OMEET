import {AfterViewInit, Component, ElementRef, OnInit, ViewChild} from '@angular/core';
import {fromEvent, merge} from 'rxjs';
import {animate, state, style, transition, trigger} from '@angular/animations';
import {MatPaginator} from '@angular/material/paginator';
import {debounceTime, distinctUntilChanged, tap} from 'rxjs/operators';
import {MatSort} from '@angular/material/sort';
import {ActivatedRoute} from '@angular/router';
import {MeetingsDataSource} from '../../../services/meetings.datasource';
import {MeetingsService} from '../../../services/meetings.service';
import {JWTTokenService} from '../../../services/JWTTokenService';

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
export class MeetingsListComponent implements OnInit, AfterViewInit{

  constructor(private route: ActivatedRoute, private meetingsService: MeetingsService, private tokenService: JWTTokenService) {}

  expandedMeeting: any;

  displayedColumns = ['id', 'name', 'status', 'date'];

  dataSource!: MeetingsDataSource;

  @ViewChild(MatPaginator, {static: true}) paginator!: MatPaginator;

  @ViewChild(MatSort, { static: true }) sort!: MatSort;

  @ViewChild('input', { static: true }) input!: ElementRef;


  ngOnInit(): void {
    this.dataSource = new MeetingsDataSource(this.meetingsService);


    (async () => {
      this.dataSource.loadMeetings(this.tokenService.getUsername(), this.input.nativeElement.value, true, 1, 3);
      await this.delay(400);      // TODO return promise
      this.paginator.length = this.dataSource.length;
    })();
  }

  private delay(ms: number): Promise<any> {
    return new Promise( resolve => setTimeout(resolve, ms) );
  }

  ngAfterViewInit(): void {

    this.sort.sortChange.subscribe(() => this.paginator.pageIndex = 0);
    console.log(this.input.nativeElement);

    fromEvent(this.input.nativeElement, 'keyup')
      .pipe(
        debounceTime(150),
        distinctUntilChanged(),
        tap(() => {
          this.paginator.pageIndex = 0;
          this.loadMeetingsPage();
        })
      )
      .subscribe();

    merge(this.sort.sortChange, this.paginator.page)
      .pipe(
        tap(() => this.loadMeetingsPage())
      )
      .subscribe();
    this.paginator.length = this.dataSource.length;
  }

  loadMeetingsPage(): void {
      this.dataSource.loadMeetings(
      this.tokenService.getUsername(),
        this.input.nativeElement.value,
      true,
      this.paginator.pageIndex + 1,
      this.paginator.pageSize
    );
      this.paginator.length = this.dataSource.length;
  }

  isExpansionDetailRow = (i: number, row: object) => row.hasOwnProperty('detailRow');

  joinMeeting(meeting: any): void{
    window.open(`https://130.61.186.61/${meeting.name}#userInfo.displayName=%22${this.tokenService.getUsername()}%22`);
  }

}


