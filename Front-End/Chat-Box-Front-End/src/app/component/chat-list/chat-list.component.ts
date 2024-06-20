import { HttpClient } from '@angular/common/http';
import { Component } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscriber, Subscription } from 'rxjs';
import { ApiRoutes } from 'src/app/Utils/api-route';
import { ReceiverListResponse } from 'src/app/payload/ReceiverList';
import { OfflineUser } from 'src/app/payload/offline-user';
import { OnlineUser } from 'src/app/payload/online-user-Request';
import { LoginService } from 'src/app/service/login.service';
import { SocketServiceService } from 'src/app/service/socket-service.service';
import { UserService } from 'src/app/service/user.service';

@Component({
  selector: 'app-chat-list',
  templateUrl: './chat-list.component.html',
  styleUrls: ['./chat-list.component.css']
})
export class ChatListComponent {
  get_messagedata!: Subscription;
  onlineUsersSubscription!: Subscription;
  messages: any[] = [];
  onlineUser: OnlineUser = new OnlineUser();
  constructor(private loginServcie: LoginService, private activeRoute: ActivatedRoute, private userService: UserService, private socketService: SocketServiceService) {

  }
  receiverList: ReceiverListResponse[] = [];
  receiverId: any;
  imageUrl = ApiRoutes.IMAGE_URL;
  status: any;
  offlineUsersSubscription!: Subscription;
  offlineUser:OfflineUser = new OfflineUser();
  ngAfterViewInit(): void {


    this.offlineUsersSubscription = this. socketService.offline_Request.subscribe((data:any)=>
      {
        this.offlineUser=data;
        console.log(this.offlineUser);
        this.updateLastSeenAfterDisconnect(this.offlineUser) 
      })

    this.onlineUsersSubscription = this.socketService.online_Request.subscribe((data: any) => {
      this.onlineUser.email = data;
      this.updateOnlineStatus();
    })

    this.status = this.socketService.typing_Request.subscribe((data: any) => {
      console.log(data);
      if(data!==null)
      this.status = data
    })
    this.get_messagedata = this.socketService.get_message.subscribe((data: any) => {
      if (data !== null) {
        let id = 0;
        console.log(data);
        if (this.loginServcie.getUserId() === data.receiverId) (
          id = data.senderId
        )
        else {
          id = data.receiverId
        }
        const index = this.receiverList.findIndex(r => r.userId === id);
        if (index !== -1) {
          this.receiverList[index].latestMsg = data.message;
          this.receiverList[index].latestMsgTime = data.createdAt;
          const receiverMoveToTop = this.receiverList.splice(index, 1)[0];
          this.receiverList.unshift(receiverMoveToTop);
        }
      }
    })
    this.activeRoute.params.subscribe((param) => {
      this.receiverId = param['rId'];
    })
    this.allReceiver();

  }

  updateOnlineStatus() {
    this.receiverList.forEach(user => {
        user.online = this.onlineUser.email.includes(user.email);
    });
}
  getUserName() {
    const user = this.loginServcie.getUser();
    return user ? user.userName : null;
  }
  getSenderId() {
    const user = this.loginServcie.getUser();
    return user ? user.userId : null;
  }

  allReceiver() {
    this.loginServcie.list(this.getUserName()).subscribe((data: any) => {
      this.receiverList = data.response;
      this.updateOnlineStatus();
    })
  }
  key: any;

  search() {

    if (this.key != '') {
      this.userService.search(this.key, this.getUserName()).subscribe((data: any) => {
        this.receiverList = data.response;
      });
    }
    else {
      this.allReceiver();
    }
  }

  updateLastSeenAfterDisconnect(offlineUser:OfflineUser)
  {
     const index = this.receiverList.findIndex(r=>r.email==offlineUser.email)
     if(index!==-1)
      {
        this.receiverList[index].latestMsgTime =offlineUser.lastSeen;
      }
  }

}
