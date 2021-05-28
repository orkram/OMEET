import {CollectionViewer, DataSource} from '@angular/cdk/collections';
import {BehaviorSubject, Observable, of, Subscription} from 'rxjs';
import {catchError, finalize} from 'rxjs/operators';
import {Contact} from '../../model/contacts';
import {UserService} from '../backend.api/UserService';


export class ContactsDataSource extends DataSource<Contact> {

  private contactsSubject = new BehaviorSubject<any>([]);
  private loadingSubject = new BehaviorSubject<boolean>(false);

  public length = 0;

  public loading$ = this.loadingSubject.asObservable();

  constructor(private userService: UserService) {
    super();
  }

  connect(): Observable<Contact[]> {
    return this.contactsSubject.asObservable();
  }

  disconnect(collectionViewer: CollectionViewer): void {
    this.contactsSubject.complete();
    this.loadingSubject.complete();
  }

  loadContacts( username: string, filter = '',
                sortDirection = true, pageIndex = 0, pageSize = 5): Subscription {

    this.loadingSubject.next(true);

    return this.userService.findContacts(username, filter, sortDirection,
      pageIndex, pageSize).pipe(
      catchError(() => of([])),
      finalize(() => this.loadingSubject.next(false))
    ).subscribe(contacts => {
      this.length = contacts.allFoundUsers;
      this.contactsSubject.next(contacts.foundUsers);
    },
      error => {
       window.location.reload();
    });
  }
}


