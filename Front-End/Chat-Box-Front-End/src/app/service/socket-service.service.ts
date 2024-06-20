import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';
import * as io from 'socket.io-client';
import { ApiRoutes } from '../Utils/api-route';

@Injectable({
  providedIn: 'root'
})
export class SocketServiceService {
  messageObservable: any;


  constructor(private http: HttpClient) { }
  private socket: any;
  private url: string = 'ws://192.168.0.13:9092';

  public get_message_data = new BehaviorSubject<any>(null);
  get_message: Observable<any> = this.get_message_data.asObservable();


  public typing_Observable = new BehaviorSubject<any>(null);
  typing_Request: Observable<any> = this.typing_Observable.asObservable();

  public online_Observable = new BehaviorSubject<any>(null);
  online_Request: Observable<any> = this.online_Observable.asObservable();

  public offline_Observable = new BehaviorSubject<any>(null);
  offline_Request: Observable<any> = this.offline_Observable.asObservable();

  public upadate_msg_status_Observable = new BehaviorSubject<any>(null);
  update_msg_status_Request: Observable<any> = this.upadate_msg_status_Observable.asObservable();

  
  connect(userEmail: any): void {
    const email = userEmail
    this.socket = io(this.url, {
      path: '/socket.io',
      transports: ['websocket'],
      query: {
        email: email,
      }
    });
    this.socket.on('connect', async () => {
      console.log('Connected to WebSocket server!');
    });
    this.socket.on('get_message', (data: any) => {
      this.get_message_data.next(data);
    });
    this.socket.on('typing_event', (data: any) => {
      this.typing_Observable.next(data);
    });
    this.socket.on('online_Users', (data: any) => {
      this.online_Observable.next(data);
    });

    this.socket.on('ofline_User', (data: any) => {
      this.offline_Observable.next(data);
    });
    this.socket.on('update_Status', (data: any) => {
      this.upadate_msg_status_Observable.next(data);
    });
    
  }
  disConnect(): void {
    if (this.socket) {
      this.socket.disconnect();
      console.log('Disconnected from WebSocket server');
    }
  }
  logOut() {
    localStorage.removeItem('sender');
    this.disConnect()
  }
  sendMessage(message: any) {
    this.socket.emit('send_message', message, (ack: string) => {
      console.log(ack);
    });
  }
  typing(userRequest: any) {
    this.socket.emit('typing', userRequest, (ack: string) => {
      console.log(ack);
    });
  }
  seenMsg(msgseen:any) {
    this.socket.emit('seen_msg',msgseen , (ack: string) => {
      console.log(ack);
    });
  }
}
