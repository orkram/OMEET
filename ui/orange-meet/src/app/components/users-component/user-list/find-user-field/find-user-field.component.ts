import {Component, EventEmitter, OnInit, Output} from '@angular/core';

@Component({
  selector: 'app-find-user-field',
  templateUrl: './find-user-field.component.html',
  styleUrls: ['./find-user-field.component.css']
})
export class FindUserFieldComponent implements OnInit {

  constructor() { }

  searchKey: string = '';

  ngOnInit(): void {
  }

  @Output() applyFilter:EventEmitter<any> = new EventEmitter()

  getInput(){
    this.applyFilter.emit(this.searchKey);
  }

}
