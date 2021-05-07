import {Component, EventEmitter, OnInit, Output} from '@angular/core';

@Component({
  selector: 'app-find-meeting',
  templateUrl: './find-meeting.component.html',
  styleUrls: ['./find-meeting.component.scss']
})
export class FindMeetingComponent implements OnInit {

  constructor() { }

  searchKey = '';

  @Output() applyFilter: EventEmitter<any> = new EventEmitter();

  ngOnInit(): void {
  }

  getInput(): void{
    this.applyFilter.emit(this.searchKey);
  }

}
