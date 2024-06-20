import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { ApiRoutes } from '../Utils/api-route';
import { BehaviorSubject, Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ConversationService {

 
  constructor(private http:HttpClient) { }

  public conversation(conversationRequest:any)
  {
    return this.http.post(ApiRoutes.CONVERSATION_CHEAK+'/',conversationRequest);
  }
}
