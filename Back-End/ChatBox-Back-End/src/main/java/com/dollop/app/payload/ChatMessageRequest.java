package com.dollop.app.payload;

import java.time.LocalDateTime;

import com.dollop.app.data.chat.MessageStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor

public class ChatMessageRequest {

	private Long id;
	private String message;
	private Long senderId;
	private Long receiverId;
	private FileData file;
    
}
