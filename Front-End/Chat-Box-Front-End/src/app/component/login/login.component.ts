import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { LoginRequest } from 'src/app/payload/LoginRequest';
import { LoginResponse } from 'src/app/payload/LoginResponse';
import { LoginService } from 'src/app/service/login.service';
import { SocketServiceService } from 'src/app/service/socket-service.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {

constructor(private loginService:LoginService,private fb:FormBuilder,private router:Router,private socketService:SocketServiceService)
{
  this.loginForm=this.fb.group(
    {
      userName:['',[Validators.required]],
      password:['',[Validators.required]]
    })
}
loginForm!:FormGroup;
loginRequest:LoginRequest = new LoginRequest();
loginResponse:LoginResponse=new LoginResponse();
login()
{
  this.loginForm.markAllAsTouched();
  if(!this.loginForm.valid)
    {
      return;
    }
     this.loginService.login(this.loginRequest).subscribe((data:any)=>{
     this.loginResponse=data.response;
     localStorage.setItem('sender',JSON.stringify(this.loginResponse))
   
     this.router.navigate(['/chat'])
   }),(error:any)=>{
    console.log('error')}  
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

}
