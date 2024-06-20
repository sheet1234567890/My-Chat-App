import { AfterViewInit, Component, ElementRef, OnDestroy, OnInit, ViewChild } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription, of } from 'rxjs';
import { ApiRoutes } from 'src/app/Utils/api-route';
import { LastSeenDatePipe } from 'src/app/custompipe/last-seen-date.pipe';
import { ChatMessageRequest } from 'src/app/payload/Chat-Message-Request';
import { ConversationRequest } from 'src/app/payload/Conversation-request';
import { ReceiverListResponse } from 'src/app/payload/ReceiverList';
import { TypingRequest } from 'src/app/payload/User-Request';
import { OfflineUser } from 'src/app/payload/offline-user';
import { OnlineUser } from 'src/app/payload/online-user-Request';
import { UpdateMessageStatus } from 'src/app/payload/update-msg-status';
import { LoginService } from 'src/app/service/login.service';
import { SocketServiceService } from 'src/app/service/socket-service.service';
import { UserService } from 'src/app/service/user.service';
@Component({
  selector: 'app-chat-content',
  templateUrl: './chat-content.component.html',
  styleUrls: ['./chat-content.component.css']
})
export class ChatContentComponent implements OnInit {

  @ViewChild('chatContainer') private chatContainer!: ElementRef;

  get_messagedata!: Subscription;
  messages: any[] = [];
  receiverName: any;
  received = ApiRoutes.RECEIVED;
  delivered = ApiRoutes.DELIVERD;
  imageUrl = ApiRoutes.IMAGE_URL;
  image: any;
  status: any;
  receiverId: any;
  chatMessageRequest: ChatMessageRequest = new ChatMessageRequest();
  r: ReceiverListResponse = new ReceiverListResponse();
  converSationRequest: ConversationRequest = new ConversationRequest();
  userRequest: TypingRequest = new TypingRequest();
  groupedMessages: { [key: string]: any[] } = {};
  onlineUsersSubscription!: Subscription;
  offlineUsersSubscription!: Subscription;
  updateMsgStatusSubscription!: Subscription;
  onlineUser: OnlineUser = new OnlineUser();
  online!: boolean;
  lastSeen: any;
  offlineUser: OfflineUser = new OfflineUser();
  updateMsgStatus: UpdateMessageStatus[] = [];
  msgSeen:any;
  constructor(
    private userService: UserService,
    private activefn: ActivatedRoute,
    private socketService: SocketServiceService,
    private loginService: LoginService,
    private lastSeenDatePipe: LastSeenDatePipe
  ) {


  }

  ngOnInit(): void {
    
    this.socketService.update_msg_status_Request.subscribe((data: any) => {
      this.updateMsgStatus = data;
      console.log('update-msg --->',this.updateMsgStatus);
       console.log('message[]  --->',this.messages);
       
      this.updateMsgStatusAfterLogin();
    });


    this.offlineUsersSubscription = this.socketService.offline_Request.subscribe((data: any) => {
      this.offlineUser = data;
      this.updateOfflineUserLastSeen(this.offlineUser)
    })

    this.onlineUsersSubscription = this.socketService.online_Request.subscribe((data: any) => {
      this.onlineUser.email = data;
      this.online = this.updateOnlineStatus();
    })
    this.status = this.socketService.typing_Request.subscribe((data: any) => {
      this.status = data;
    });
    this.get_messagedata = this.socketService.get_message.subscribe((data: any) => {
      if (data !== null) {
        this.messages.push(data);
        this.groupMessagesByDate();
        this.handleScroller();
      }
    });
    this.activefn.params.subscribe((param) => {
      this.receiverId = param['rId'];
      this.converSationRequest.receiverId = this.receiverId;
      this.chatMessageRequest.receiverId = this.receiverId;
      this.receiver();
    });
    this.chatMessageRequest.senderId = this.loginService.getUserId();
  }

  receiver() {
    this.converSationRequest.senderId = this.loginService.getUserId();
    this.userService.receiver(this.converSationRequest).subscribe((data: any) => {
      this.userRequest.conversationId = data.Conversation_Id;
      this.userRequest.email = data.email;
      this.messages = data.data;
      this.receiverName = data.receiverName;
      this.image = data.image;
      this.lastSeen = data.lastSeen;
      this.messages.forEach(message => message.createdAt = new Date(message.createdAt));
      this.groupMessagesByDate();
      this.online = this.updateOnlineStatus();
      this.updateSeenMessageStatus();
    });
  }
  s:any;
  updateSeenMessageStatus()
  {
   
    this.socketService.seenMsg(this.s);
  }
  updateOnlineStatus() {
    return this.onlineUser.email.includes(this.userRequest.email);
  }
  sendMessage() {
    if (this.chatMessageRequest.message.trim() || this.chatMessageRequest.file.length > 0) {
      this.socketService.sendMessage(this.chatMessageRequest);
      this.chatMessageRequest.message = '';
      this.chatMessageRequest.file = [];
      this.handleScroller();
      this.userRequest.status = false;
      this.typing();
    }
  }
  getClass(userId: any) {
    return userId === this.loginService.getUserId()
      ? 'chatbox_message d-flex justify-content-end align-items-center mb-2'
      : 'chatbox_message d-flex justify-content-start mb-2 ';
  }
getSenderId()
{
  return this.loginService.getUserId();
}
  handleScroller() {
    const container = this.chatContainer.nativeElement;
    container.scrollTop = container.scrollHeight;
  }
  typing() {
    this.socketService.typing(this.userRequest);
  }
  groupMessagesByDate() {
    this.groupedMessages = {};
    const today = new Date();
    const yesterday = new Date(today);
    yesterday.setDate(today.getDate() - 1);
    this.messages.forEach(message => {
      const messageDate = new Date(message.createdAt);
      let dateKey;
      if (this.isSameDay(messageDate, today)) {
        dateKey = 'Today';
      } else if (this.isSameDay(messageDate, yesterday)) {
        dateKey = 'Yesterday';
      } else {
        dateKey = this.formatDate(messageDate)
      }
      if (!this.groupedMessages[dateKey]) {
        this.groupedMessages[dateKey] = [];
      }
      this.groupedMessages[dateKey].push(message);
    });
  }
  formatDate(date: any) {
    const months = ['January', 'February', 'March', 'April', 'May', 'June', 'July', 'August', 'September', 'October', 'November', 'December'];
    let day = date.getDate();
    let month = months[date.getMonth()];
    let year = date.getFullYear();
    return `${day} ${month} ${year}`;
  }
 isSameDay(date1: Date, date2: Date): boolean {
    return date1.getFullYear() === date2.getFullYear() &&
      date1.getMonth() === date2.getMonth() &&
      date1.getDate() === date2.getDate();
  }

  getSortedDates(): string[] {
    return Object.keys(this.groupedMessages).sort((a, b) => {
      if (a === 'Today' || b === 'Today') return a === 'Today' ? 1 : -1;
      if (a === 'Yesterday' || b === 'Yesterday') return a === 'Yesterday' ? 1 : -1;
      return new Date(a).getTime() - new Date(b).getTime();
    });
  }
  updateOfflineUserLastSeen(offlineuser: OfflineUser) {
    if (this.userRequest.email === offlineuser.email) {
      this.lastSeen = offlineuser.lastSeen;
    }
  }
  updateMsgStatusAfterLogin() {
    this.messages.forEach(m => {
      const statusUpdate = this.updateMsgStatus.find(update => update.id === m.id);
      if (statusUpdate) {
        m.status = statusUpdate.status;
      }
    }); 
  }
  
}

