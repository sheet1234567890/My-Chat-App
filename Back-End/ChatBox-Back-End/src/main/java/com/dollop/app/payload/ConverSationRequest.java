package com.dollop.app.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConverSationRequest {

	private Long senderId;
	private Long receiverId;
}
