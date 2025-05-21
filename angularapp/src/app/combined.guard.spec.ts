import { TestBed } from '@angular/core/testing';

import { CombinedGuard } from './combined.guard';

describe('CombinedGuard', () => {
  let guard: CombinedGuard;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    guard = TestBed.inject(CombinedGuard);
  });

  it('should be created', () => {
    expect(guard).toBeTruthy();
  });
});
