import {ComponentFixture, TestBed} from '@angular/core/testing';

import {CreateMeetingDialogComponent} from './create-meeting-dialog.component';

describe('CreateMeetingDialogComponent', () => {
  let component: CreateMeetingDialogComponent;
  let fixture: ComponentFixture<CreateMeetingDialogComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ CreateMeetingDialogComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(CreateMeetingDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
