import {Component, OnInit, TemplateRef, ViewChild} from '@angular/core';
import {endOfDay, isSameDay, isSameMonth, startOfDay} from 'date-fns';
import {Subject} from 'rxjs';
import {NgbModal} from '@ng-bootstrap/ng-bootstrap';
import {
  CalendarDateFormatter,
  CalendarEvent,
  CalendarEventAction,
  CalendarEventTimesChangedEvent,
  CalendarView,
  DAYS_OF_WEEK
} from 'angular-calendar';
import {Router} from '@angular/router';
import {MeetingsService} from '../../services/backend.api/meetings.service';
import {JWTTokenService} from '../../services/auth/JWTTokenService';
import {CustomDateFormatter} from '../../services/CustomDateFormatter';

const colors: any = {
  red: {
    primary: '#ad2121',
    secondary: '#FAE3E3',
  },
  blue: {
    primary: '#1e90ff',
    secondary: '#D1E8FF',
  },
  yellow: {
    primary: '#e3bc08',
    secondary: '#FDF1BA',
  },
};

@Component({
  selector: 'app-calendar',
  templateUrl: './calendar.component.html',
  styleUrls: ['./calendar.component.scss'],
  providers: [{
    provide: CalendarDateFormatter,
    useClass: CustomDateFormatter
  }
  ]
})
export class CalendarComponent implements OnInit{
  // @ts-ignore
  @ViewChild('modalContent', { static: true }) modalContent: TemplateRef<any>;

  view: CalendarView = CalendarView.Month;

  CalendarView = CalendarView;

  viewDate: Date = new Date();

  // @ts-ignore
  modalData: {
    action: string;
    event: CalendarEvent;
  };

  colors: any = {
    red: {
      primary: '#ad2121',
      secondary: '#FAE3E3',
    },
    blue: {
      primary: '#1e90ff',
      secondary: '#D1E8FF',
    },
    yellow: {
      primary: '#e3bc08',
      secondary: '#FDF1BA',
    },
  };


  actions: CalendarEventAction[] = [
    {
      label: '<i class="fas fa-fw fa-trash-alt"></i>',
      a11yLabel: 'Delete',
      cssClass: 'icons',
      onClick: ({ event }: { event: CalendarEvent }): void => {
        this.events = this.events.filter((iEvent) => iEvent !== event);
        this.meetingsService.cancelMeeting(event.id).subscribe();
        this.handleEvent('Deleted', event);
      },
    },
    {
      label: '<i class="fas text-icon fa-door-open"></i>',
      a11yLabel: 'Join',
      cssClass: 'icons',
      onClick: ({ event }: { event: CalendarEvent }): void => {
        console.log(event);
        this.router.navigateByUrl(`/meeting/${event.id}`);
        this.handleEvent('Join', event);
      },
    }
  ];

  refresh: Subject<any> = new Subject();

  events: CalendarEvent[] = [];

  activeDayIsOpen = true;

  locale = 'en';

  weekStartsOn: number = DAYS_OF_WEEK.MONDAY;

  weekendDays: number[] = [DAYS_OF_WEEK.FRIDAY, DAYS_OF_WEEK.SATURDAY];

  constructor(
    private modal: NgbModal,
    private router: Router,
    private meetingsService: MeetingsService,
    private tokenService: JWTTokenService
  ) {}

  dayClicked({ date, events }: { date: Date; events: CalendarEvent[] }): void {
    if (isSameMonth(date, this.viewDate)) {
      this.activeDayIsOpen = !((isSameDay(this.viewDate, date) && this.activeDayIsOpen) ||
        events.length === 0);
      this.viewDate = date;
    }
  }

  eventTimesChanged(
    {
    event,
    newStart,
    newEnd,
  }: CalendarEventTimesChangedEvent): void {
    this.events = this.events.map((iEvent) => {
      if (iEvent === event) {
        return {
          ...event,
          start: newStart,
          end: newEnd,
        };
      }
      return iEvent;
    });

    console.log(event);
    if (event.id) {
      this.meetingsService.updateMeeting(event.id.toString(), newStart, event.title, this.tokenService.getUsername()).subscribe(); }
    this.handleEvent('Dropped or resized', event);
  }

  handleEvent(action: string, event: CalendarEvent): void {
    this.modalData = { event, action };
    this.modal.open(this.modalContent, { size: 'lg' });
  }

  addEvent(): void {
    this.events = [
      ...this.events,
      {
        title: 'New event',
        start: startOfDay(new Date()),
        end: endOfDay(new Date()),
        color: colors.red,
        draggable: true,
        resizable: {
          beforeStart: true,
          afterEnd: true,
        },
      },
    ];
  }

  setView(view: CalendarView): void {
    this.view = view;
  }

  closeOpenMonthViewDay(): void {
    this.activeDayIsOpen = false;
  }

  ngOnInit(): void {
     this.meetingsService.getAllMeetings(this.tokenService.getUsername()).subscribe(
      next => {
                this.events = next.map( (meeting: any) => {
                  return {
                    id: meeting.idMeeting,
                    start: new Date(meeting.date),
                    title: meeting.name,
                    color: colors.red,
                    actions: this.actions,
                    resizable: {
                      beforeStart: true,
                      afterEnd: true,
                    },
                    draggable: true,
                  };
                }
      );
      }
    );
  }
}
