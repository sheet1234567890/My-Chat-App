import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import {HttpClientModule }from '@angular/common/http'; 
import { NavbarComponent } from './component/navbar/navbar.component';
import { ChatListComponent } from './component/chat-list/chat-list.component';
import { ChatContentComponent } from './component/chat-content/chat-content.component';
import { RouterModule } from '@angular/router';
import { LoginComponent } from './component/login/login.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { ChatDeshboardComponent } from './component/chat-deshboard/chat-deshboard.component';
import { LastSeenDatePipe } from './custompipe/last-seen-date.pipe';

@NgModule({
  declarations: [
    AppComponent,
    NavbarComponent,
    ChatListComponent,
    ChatContentComponent,
    LoginComponent,
    ChatDeshboardComponent,
    LastSeenDatePipe
  ],
  imports: [
    RouterModule,
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    FormsModule,
    ReactiveFormsModule ,
  ],
  providers: [LastSeenDatePipe],
  bootstrap: [AppComponent]
})
export class AppModule { }
