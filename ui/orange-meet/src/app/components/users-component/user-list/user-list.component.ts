import {AfterViewInit, Component, ElementRef, OnInit, ViewChild} from '@angular/core';
import {MatPaginator} from '@angular/material/paginator';
import {MatSort} from '@angular/material/sort';
import {ActivatedRoute} from '@angular/router';
import {JWTTokenService} from '../../../services/JWTTokenService';
import {ContactsDataSource} from '../../../services/ContactsDataSource';
import {UserService} from '../../../services/UserService';
import {fromEvent, merge} from 'rxjs';
import {debounceTime, distinctUntilChanged, tap} from 'rxjs/operators';
import {SelectedUsersService} from '../../../services/SelectedUsersService';

@Component({
  selector: 'app-user-list',
  templateUrl: './user-list.component.html',
  styleUrls: ['./user-list.component.scss']
})

export class UserListComponent implements OnInit, AfterViewInit {

  constructor(
    private route: ActivatedRoute,
    private userService: UserService,
    private tokenService: JWTTokenService,
    private selectedUsersService: SelectedUsersService
  ) {}

  displayedColumns = ['image', 'name', 'email', 'checked'];

  dataSource!: ContactsDataSource;

  @ViewChild(MatPaginator, {static: true}) paginator!: MatPaginator;

  @ViewChild(MatSort, { static: true }) sort!: MatSort;

  @ViewChild('input', { static: true }) input!: ElementRef;

  imagePlaceholder = '../../../../assets/images/small-user-image.png';

  innerHeight: any;

  selected: {[key: string]: boolean} = {};

  defaultPageSize = 5;

  selectedUsers(): Array<string>{
  return Object.keys(this.selected).filter((username: string) => this.selected[username]);
}

  onSelect(checked: boolean, contact: any): void{
   this.selected[contact.userName] = checked;
   this.selectedUsersService.setSelectedUsers(this.selectedUsers());
   console.log(this.selectedUsers());
  }

  defaultPageSizes(): Array<number> {
    return this.innerHeight > 1200 ? [5, 12] : [5];
  }

  private delay(ms: number): Promise<any> {
    return new Promise( resolve => setTimeout(resolve, ms) );
  }

  ngOnInit(): void {
    this.innerHeight = window.innerHeight;
    this.dataSource = new ContactsDataSource(this.userService);

    (async () => {
      this.dataSource.loadContacts(this.tokenService.getUsername(), this.input.nativeElement.value, true, 1, this.defaultPageSize);
      await this.delay(400);      // TODO return promise
      this.paginator.length = this.dataSource.length;
    })();
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
    this.dataSource.loadContacts(
      this.tokenService.getUsername(),
      this.input.nativeElement.value,
      true,
      this.paginator.pageIndex + 1,
      this.paginator.pageSize
    );
    this.paginator.length = this.dataSource.length;
  }
}


