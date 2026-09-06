import { ComponentFixture, TestBed } from '@angular/core/testing';

import { NavbarWelcomepageComponent } from './navbar-welcomepage.component';

describe('NavbarWelcomepageComponent', () => {
  let component: NavbarWelcomepageComponent;
  let fixture: ComponentFixture<NavbarWelcomepageComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [NavbarWelcomepageComponent]
    });
    fixture = TestBed.createComponent(NavbarWelcomepageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
