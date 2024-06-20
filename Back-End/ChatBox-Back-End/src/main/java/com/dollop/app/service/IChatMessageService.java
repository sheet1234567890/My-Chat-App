package com.dollop.app.service;

import org.springframework.http.ResponseEntity;

import com.dollop.app.data.chat.ChatMessage;
import com.dollop.app.payload.ChatMessageRequest;
import com.dollop.app.payload.FileSendingRequest;
import com.dollop.app.payload.LastMessageResponse;

public interface IChatMessageService {

	public ResponseEntity<?> sendMessage(ChatMessageRequest request);
	public ResponseEntity<?> lastMessage(String cId);
	
}
