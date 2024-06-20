import { Component, OnInit } from '@angular/core';
import { SocketServiceService } from './service/socket-service.service';
import { UserService } from './service/user.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit {
  title = 'Chat-Box-Front-End';
  constructor(private socketService:SocketServiceService,private userService:UserService){

  }
  ngOnInit(): void {
    
  }
  
}
