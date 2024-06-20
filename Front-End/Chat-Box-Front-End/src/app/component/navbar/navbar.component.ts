import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import Toasts from 'src/app/Utils/Toast';
import { ApiRoutes } from 'src/app/Utils/api-route';
import { LoginResponse } from 'src/app/payload/LoginResponse';
import { LoginService } from 'src/app/service/login.service';
import { SocketServiceService } from 'src/app/service/socket-service.service';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.css']
})
export class NavbarComponent implements OnInit {
constructor(private loginService:LoginService,private router:Router,private socket:SocketServiceService)
{

}
  ngOnInit(): void {
   this.getUser();
  }
loginUser :LoginResponse = new LoginResponse();
imageUrl=ApiRoutes.IMAGE_URL;
getUser()
{
 this.loginUser= this.loginService.getUser();
}

logOut()
{
  Swal.fire({
    title:"Do you want to Logout your account?",
    showDenyButton: true,
    confirmButtonText: "Logout",
    confirmButtonColor: 'red',
    denyButtonText: `Don't Logout`,
    denyButtonColor: 'blue',
    icon: 'info'
  }).then((result)=>{
    if(result.isConfirmed)
      {
        this.socket.logOut()
        Toasts.fire({
          icon:'success',
          text:'Logout sucessfully...',
          timer:1000
        })
        this.router.navigate([''])
      }else if(result.isDenied)
        {
          Toasts.fire({
            icon: 'info',
            text: 'Logout Canceled...',
            timer: 1000
          })
        }
  })
}

}
