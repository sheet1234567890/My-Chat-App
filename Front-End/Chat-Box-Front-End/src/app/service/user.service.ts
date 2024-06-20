import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { ApiRoutes } from '../Utils/api-route';
import { BehaviorSubject, Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class UserService {

 
  constructor(private http:HttpClient) { }

  public receiver(conversationRequest:any)
  {
    return this.http.post(ApiRoutes.RECEIVER_BY_ID+``,conversationRequest);
  }

  public search(key:any,userName:any)
  {
    return this.http.get(ApiRoutes.SEARCH+`${key}/${userName}`);
  }
}
