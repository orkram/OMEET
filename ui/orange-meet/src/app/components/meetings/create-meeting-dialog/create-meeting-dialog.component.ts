import {Component, ElementRef, OnInit, ViewChild} from '@angular/core';
import {FormControl, FormGroup, Validators} from '@angular/forms';
import {JWTTokenService} from '../../../services/auth/JWTTokenService';
import {MeetingsService} from '../../../services/backend.api/meetings.service';
import {MatDialogRef} from '@angular/material/dialog';
import {Observable} from 'rxjs';
import {map, startWith} from 'rxjs/operators';
import {UserService} from '../../../services/backend.api/UserService';

@Component({
  selector: 'app-create-meeting-dialog',
  templateUrl: './create-meeting-dialog.component.html',
  styleUrls: ['./create-meeting-dialog.component.scss']
})
export class CreateMeetingDialogComponent implements OnInit {

  constructor(
    private tokenService: JWTTokenService,
    private meetingService: MeetingsService,
    private dialogRef: MatDialogRef<CreateMeetingDialogComponent>,
    private userService: UserService
  ) { }

  form: FormGroup = new FormGroup({
    name: new FormControl('', Validators.compose([Validators.required])),
    description: new FormControl('', Validators.compose([Validators.required])),
    participants: new FormControl(''),
  });

  usersToAdd: string[] = [this.tokenService.getUsername()];

  searchTextboxControl: FormControl = new FormControl();

  submitted = false;

  errorMessage = false;

  @ViewChild('search')
  searchTextBox!: ElementRef;

  selectFormControl = new FormControl();
  selectedValues: any = [];
  data: any[] = [{
    lastName: '',
    firstName: ''
  }];


  filteredOptions!: Observable<Array<any>>;

  ngOnInit(): void {

    this.userService.findAllContacts(this.tokenService.getUsername(), '').subscribe(
      next => { this.data = next; }
    );

    this.filteredOptions = this.searchTextboxControl.valueChanges
      .pipe(
        startWith<string>(''),
        map(name => this._filter(name))
      );
  }


  private _filter(name: string): string[] {
    const filterValue = name.toLowerCase();
    this.setSelectedValues();
    this.selectFormControl.patchValue(this.selectedValues);
    return this.data.filter(option => option.firstName.toLowerCase().indexOf(filterValue) === 0 ||
      option.lastName.toLowerCase().indexOf(filterValue) === 0);
  }

  selectionChange(event: any): void {
    if (event.isUserInput && event.source.selected === false) {
      const index = this.selectedValues.indexOf(event.source.value);
      this.selectedValues.splice(index, 1);
    }
  }

  openedChange(e: any): void {
    this.searchTextboxControl.patchValue('');
    // Focus to search textbox while clicking on selectbox
    if (e === true) {
      this.searchTextBox.nativeElement.focus();
    }
  }


  clearSearch(event: any): void {
    event.stopPropagation();
    this.searchTextboxControl.patchValue('');
  }


  setSelectedValues(): void {
    if (this.selectFormControl.value && this.selectFormControl.value.length > 0) {
      this.selectFormControl.value.forEach((e: any) => {
        if (this.selectedValues.indexOf(e) === -1) {
          this.selectedValues.push(e);
        }
      });
    }
  }

  submit(): void{
    if (this.form.invalid) {
      return;
    } else {
      this.submitted = true;
      this.meetingService
        .createMeeting(
          this.tokenService.getUsername(),
          this.form.value.name,
          this.selectFormControl.value
        )
        .subscribe(
        res => { console.log(res); },
        _ => {
          this.submitted = false;
          this.errorMessage = true;
        },
          () => {
          this.dialogRef.close();
          window.location.reload();
        }
      );
    }
  }

}
