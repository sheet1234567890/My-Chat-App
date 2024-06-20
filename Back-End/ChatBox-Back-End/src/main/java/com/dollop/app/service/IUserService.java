package com.dollop.app.service;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import com.dollop.app.payload.ChatMessageStatusUpdateResponse;
import com.dollop.app.payload.LoginRequest;
import com.dollop.app.payload.OfflineUserWithLastSeen;
import com.dollop.app.payload.UserRequest;

public interface IUserService {

	public ResponseEntity<?> addUser(String userRequest,MultipartFile profile);
	public ResponseEntity<?> loginUser(LoginRequest request);
	public ResponseEntity<?> reciverList(String email);
	public ResponseEntity<?> receiverContent(Long receiverId);
	public ResponseEntity<?> searchReceiver(String key,String userName);
	public ResponseEntity<?> isOnlineStatusUpdate(String email,Boolean status);
	public OfflineUserWithLastSeen isOfflinegetLastSeen(String email);
	public List<ChatMessageStatusUpdateResponse> updateMessageStatus(String email);
}
