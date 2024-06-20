package com.dollop.app.payload;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class FileSendingRequest {

	private Long id;
	private List< String> files;
	private Long senderId;
	private Long receiverId;

}
