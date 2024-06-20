import { DatePipe } from '@angular/common';
import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'lastSeenDate'
})
export class LastSeenDatePipe implements PipeTransform {

  transform(value: string): unknown {
    const currentDate = new Date();
    const lastSeenDate = new Date(value);
    const datePipe = new DatePipe('en-US');

    //if date is today
    if(this.isSameDate(currentDate,lastSeenDate))
      {
        return 'Last seen today at '+datePipe.transform(lastSeenDate,'h.mm a');
      }

    //if date is yesterday
    const yesterday = new Date(currentDate);
    yesterday.setDate(currentDate.getDate()-1)
    if(this.isSameDate(yesterday,lastSeenDate))  {
    return 'Last seen yesterday at '+datePipe.transform(lastSeenDate,'h.mm a');
    }
   // if date is within the same week
   if(lastSeenDate>=new Date(currentDate.getTime()-6*24*60*60*1000)){
   return 'Last seen '+this.getDayName(lastSeenDate.getDay())+ ' at '+ datePipe.transform(lastSeenDate, 'h.mm a');
   }

  //  if date is beyond a week
  return 'Last seen '+ datePipe.transform(lastSeenDate,'d MMM h.mm a')

  }



  private isSameDate(date1:Date,date2:Date)
  {
    return date1.getFullYear()===date2.getFullYear()&&
           date1.getMonth()===date2.getMonth()&&
           date1.getDate()===date2.getDate();
  }
  private getDayName(day: number): string {
    const days = ['Sun', 'Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat'];
    return days[day];
  }
}
