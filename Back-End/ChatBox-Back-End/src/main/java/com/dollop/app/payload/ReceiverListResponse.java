package com.dollop.app.payload;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ReceiverListResponse {

	private Long userId;
	private String userName;
	private String profile;
	private LocalDateTime createdAt;
	private String converSationId;
	private String latestMsg;
	private LocalDateTime latestMsgTime;
	private String email;
}
