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
public class UserRequest {

	private String userId;
	private String userName;
	private String email;
	private Boolean isActive=true;
	private LocalDateTime createdAt;
	private String password;
}
