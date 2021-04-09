import {Component, EventEmitter, OnInit, Output} from '@angular/core';

@Component({
  selector: 'app-find-meeting',
  templateUrl: './find-meeting.component.html',
  styleUrls: ['./find-meeting.component.css']
})
export class FindMeetingComponent implements OnInit {

  constructor() { }

  searchKey = '';

  ngOnInit(): void {
  }

  @Output() applyFilter:EventEmitter<any> = new EventEmitter()

  getInput(){
    this.applyFilter.emit(this.searchKey);
  }

}
