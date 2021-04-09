import { ComponentFixture, TestBed } from '@angular/core/testing';

import { FindMeetingComponent } from './find-meeting.component';

describe('FindMeetingComponent', () => {
  let component: FindMeetingComponent;
  let fixture: ComponentFixture<FindMeetingComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ FindMeetingComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(FindMeetingComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
