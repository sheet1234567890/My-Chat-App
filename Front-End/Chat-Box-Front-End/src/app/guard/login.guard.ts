import { Injectable } from '@angular/core';
import { CanActivate, CanActivateFn, Router } from '@angular/router';
import { LoginService } from '../service/login.service';
import { SocketServiceService } from '../service/socket-service.service';



@Injectable({
  providedIn: 'root'
})
export class loginGuard implements CanActivate {
  constructor(private router: Router,private loginService:LoginService,private socketService:SocketServiceService) { }
 
  canActivate(): boolean {

    if(this.loginService.isLoggin())
    {
      this.socketService.connect(this.loginService.getUserEmail());
      return true
    }
   
    this.router.navigate(['/login'])
    return false;
  }

};