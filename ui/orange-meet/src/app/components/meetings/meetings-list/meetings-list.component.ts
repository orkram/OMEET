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
import {ParticipantsService} from '../../../services/ParticipantsService';
import {SettingsService} from '../../../services/SettingsService';

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

  constructor(
    private route: ActivatedRoute,
    private meetingsService: MeetingsService,
    private tokenService: JWTTokenService,
    private participantsService: ParticipantsService,
    private settings: SettingsService) {}

  expandedMeeting: any;

  displayedColumns = ['id', 'name', 'status', 'date'];

  dataSource!: MeetingsDataSource;

  @ViewChild(MatPaginator, {static: true}) paginator!: MatPaginator;

  @ViewChild(MatSort, { static: true }) sort!: MatSort;

  @ViewChild('input', { static: true }) input!: ElementRef;

  defaultPageSize = 7;

  ngOnInit(): void {
    this.dataSource = new MeetingsDataSource(this.meetingsService, this.participantsService);

    (async () => {
      this.dataSource.loadMeetings(this.tokenService.getUsername(), this.input.nativeElement.value, true, 1, this.defaultPageSize);
      await this.delay(300);                // TODO return promise
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
      .subscribe(
        _ => {},
        _ => {window.location.reload(); }
      );
    this.paginator.length = this.dataSource.length;
  }

  getParticipants(id: string): Array<string>{
    const part = this.participantsService
      .getParticipants(id)
      .subscribe(
        next => { console.log(next); }
      );
    return [];
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

    let micMuted = false;
    let VidMuted = false;
    this.settings.getSettings(this.tokenService.getUsername()).subscribe(
      next => {
        micMuted = !next.defaultMicOn;
        VidMuted = next.defaultCamOn;
      }
    );

    window.open(`https://130.61.186.61/${meeting.idMeeting}#userInfo.displayName=%22${this.tokenService.getEmail()}%22&config.subject="Test meeting"&config.startWithVideoMuted=${VidMuted}&config.startSilent=false`);
  }

}

