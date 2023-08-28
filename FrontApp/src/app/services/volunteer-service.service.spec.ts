import { TestBed } from '@angular/core/testing';

import { VolunteerServiceService } from './volunteerservice';

describe('VolunteerServiceService', () => {
  let service: VolunteerServiceService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(VolunteerServiceService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
