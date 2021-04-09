import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-settings',
  templateUrl: './settings.component.html',
  styleUrls: ['./settings.component.css']
})
export class SettingsComponent implements OnInit {

  constructor() { }
  availableDevices: string[] = [
    'System default device',
    'Alternative device 2',
    'Alternative device 3'];


  ngOnInit(): void {
  }

  onFileSelected(){

  }
}
