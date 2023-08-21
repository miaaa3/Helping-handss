import { ComponentFixture, TestBed } from '@angular/core/testing';

import { LoginVolunteerComponent } from './login-volunteer.component';

describe('LoginVolunteerComponent', () => {
  let component: LoginVolunteerComponent;
  let fixture: ComponentFixture<LoginVolunteerComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [LoginVolunteerComponent]
    });
    fixture = TestBed.createComponent(LoginVolunteerComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
