package com.dollop.app.payload;

import com.dollop.app.data.chat.MessageStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ChatMessageStatusUpdateResponse {

	private Long id;
	private MessageStatus status;
	private Long receiverId;
	private String message;
	private String receiverEmail;
	private String conversationId;
}
