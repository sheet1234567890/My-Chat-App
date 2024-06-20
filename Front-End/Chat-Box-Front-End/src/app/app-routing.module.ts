import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { LoginComponent } from './component/login/login.component';
import { AppComponent } from './app.component';
import { ChatDeshboardComponent } from './component/chat-deshboard/chat-deshboard.component';
import { ChatContentComponent } from './component/chat-content/chat-content.component';
import { ChatListComponent } from './component/chat-list/chat-list.component';
import { loginGuard } from './guard/login.guard';

const routes: Routes = [
  {path:'',component:LoginComponent},
  {
    path:'chat',
    component:ChatDeshboardComponent,
    canActivate:[loginGuard],
    children:[
      {
        path:'receiver/:rId',component:ChatContentComponent
      }]
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
