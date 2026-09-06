import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RegistrationPageOrganizationComponent } from './registration-page-organization.component';

describe('RegistrationPageOrganizationComponent', () => {
  let component: RegistrationPageOrganizationComponent;
  let fixture: ComponentFixture<RegistrationPageOrganizationComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [RegistrationPageOrganizationComponent]
    });
    fixture = TestBed.createComponent(RegistrationPageOrganizationComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
