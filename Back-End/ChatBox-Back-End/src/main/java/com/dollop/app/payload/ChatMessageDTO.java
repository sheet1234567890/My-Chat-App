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
public class ChatMessageDTO {

	private Long userId;
	private MessageStatus chatMessageStatus;
	private String message;

	
}
