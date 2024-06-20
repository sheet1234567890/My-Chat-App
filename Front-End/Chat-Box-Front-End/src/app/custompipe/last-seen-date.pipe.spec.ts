import { LastSeenDatePipe } from './last-seen-date.pipe';

describe('LastSeenDatePipe', () => {
  it('create an instance', () => {
    const pipe = new LastSeenDatePipe();
    expect(pipe).toBeTruthy();
  });
});
