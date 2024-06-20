import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { ApiRoutes } from '../Utils/api-route';
import { LoginRequest } from '../payload/LoginRequest';
import { BehaviorSubject } from 'rxjs/internal/BehaviorSubject';
import { Observable } from 'rxjs/internal/Observable';
import { SocketServiceService } from './socket-service.service';

@Injectable({
  providedIn: 'root'
})
export class LoginService {

  constructor(private http:HttpClient,private socket:SocketServiceService) { }

  public chatsSubject = new BehaviorSubject<any>(null);
  chats:Observable<any>=this.chatsSubject.asObservable();

  public activeChatSubject = new BehaviorSubject<any>(null);
  activeChat:Observable<any>=this.activeChatSubject.asObservable();

selectChat(chat:any){
this.activeChatSubject.next(chat);

}


  public login(loginRequest:any)
  {
    return this.http.post(ApiRoutes.LOGIN,loginRequest);
  }
  public list(userName:any)
  {
    return this.http.get(ApiRoutes.RECEIVER_LIST+`${userName}`)
  }
  public isLoggin()
  {
   const user=this.getUser();
    if(!user||user==''||user===null)
      {
          return false;
      }
      return true;
  }
  getUser()
{
  const userString = localStorage.getItem('sender');
  return userString?JSON.parse(userString):null;
}
getUserEmail()
{
  const user = this.getUser();
  return user?user.email:null;
}
getUserName()
{
  const user = this.getUser();
  return user?user.userName:null;
}
getUserId()
{
  const user = this.getUser();
  return user?user.userId:null;
}

}
