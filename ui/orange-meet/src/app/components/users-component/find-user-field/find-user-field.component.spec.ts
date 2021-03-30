import { ComponentFixture, TestBed } from '@angular/core/testing';

import { FindUserFieldComponent } from './find-user-field.component';

describe('FindUserFieldComponent', () => {
  let component: FindUserFieldComponent;
  let fixture: ComponentFixture<FindUserFieldComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ FindUserFieldComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(FindUserFieldComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
