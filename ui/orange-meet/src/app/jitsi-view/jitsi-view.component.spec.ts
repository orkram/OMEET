import {ComponentFixture, TestBed} from '@angular/core/testing';

import {JitsiViewComponent} from './jitsi-view.component';

describe('JitsiViewComponent', () => {
  let component: JitsiViewComponent;
  let fixture: ComponentFixture<JitsiViewComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ JitsiViewComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(JitsiViewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
