import {Component, OnInit, ViewChild} from '@angular/core';
import {MatPaginator} from "@angular/material/paginator";
import {MatTableDataSource} from "@angular/material/table";
import {MatSort} from "@angular/material/sort";

@Component({
  selector: 'app-user-list',
  templateUrl: './user-list.component.html',
  styleUrls: ['./user-list.component.css']
})

export class UserListComponent implements OnInit {

  constructor() {
    this.dataSource = new MatTableDataSource<any>();
  }

  headerRow: string[] = ['image','name','email','checked'];

  dataSource: MatTableDataSource<any> ;

  @ViewChild(MatPaginator, {static: true})
  paginator!: MatPaginator;

  @ViewChild(MatSort, {static: false}) set content(sort: MatSort) {
    this.dataSource.sort = sort;
  }
  imagePlaceholder: string = '../../../../assets/images/small-user-image.png';

  contacts: Contact[] = [
    new Contact(this.imagePlaceholder,'John Doe', 'johnDoe@mail.com'),
    {image: this.imagePlaceholder, name: 'Carnegie Mondover',email: 'CarnegieMondover@mail.com', checked: false},
    {image: this.imagePlaceholder,name: 'Lance Bogrol',email: 'LanceBogrol@mail.com', checked: false},
    {image: this.imagePlaceholder,name: 'Lance Bogrol',email: 'LanceBogrol@mail.com', checked: false},
    {image: this.imagePlaceholder,name: 'Lance Bogrol',email: 'LanceBogrol@mail.com', checked: false},
    {image: this.imagePlaceholder,name: 'Lance Bogrol',email: 'LanceBogrol@mail.com', checked: false},
    {image: this.imagePlaceholder,name: 'Lance Bogrol',email: 'LanceBogrol@mail.com', checked: false},
    {image: this.imagePlaceholder,name: 'Lance Bogrol',email: 'LanceBogrol@mail.com', checked: false},
    {image: this.imagePlaceholder,name: 'Lance Bogrol',email: 'LanceBogrol@mail.com', checked: false},
    {image: this.imagePlaceholder,name: 'Lance Bogrol',email: 'LanceBogrol@mail.com', checked: false}
  ];

  selectedContacts(){
    return this.dataSource.data.filter(contact => contact.checked).length
  }

  ngOnInit(): void {

    this.dataSource = new MatTableDataSource<Contact>(this.contacts);
    this.dataSource.paginator = this.paginator;
  }

  applyFilter(key: string){
      console.log(key);
      this.dataSource.filter = key.trim().toLocaleLowerCase();
  }

}

export class Contact {
  image: string;
  name: string;
  email: string;
  checked: boolean = false;

  constructor(image:string,name:string, email:string){
    this.image = image;
    this.name = name;
    this.email = email;
  }
}

