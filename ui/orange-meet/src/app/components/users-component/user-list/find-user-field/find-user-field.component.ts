import {Component, EventEmitter, OnInit, Output} from '@angular/core';

@Component({
  selector: 'app-find-user-field',
  templateUrl: './find-user-field.component.html',
  styleUrls: ['./find-user-field.component.scss']
})
export class FindUserFieldComponent implements OnInit {

  constructor() { }

  searchKey = '';

  @Output() applyFilter: EventEmitter<any> = new EventEmitter();

  ngOnInit(): void {
  }

  // tslint:disable-next-line:typedef
  getInput(){
    this.applyFilter.emit(this.searchKey);
  }

}
