package com.dollop.app.serviceImpl;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.dollop.app.data.User;
import com.dollop.app.data.chat.ChatMessage;
import com.dollop.app.data.chat.Conversation;
import com.dollop.app.data.chat.MessageStatus;
import com.dollop.app.payload.ChatMessageStatusUpdateResponse;
import com.dollop.app.payload.LoginRequest;
import com.dollop.app.payload.LoginResponse;
import com.dollop.app.payload.OfflineUserWithLastSeen;
import com.dollop.app.payload.ReceiverListResponse;
import com.dollop.app.repo.IChatMessageRepo;
import com.dollop.app.repo.IConverSationRepo;
import com.dollop.app.repo.IUserRepo;
import com.dollop.app.service.IUserService;
import com.dollop.app.util.AppConstant;
import com.dollop.app.util.IImageService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
@Service
public class UserServiceImpl implements IUserService{

	@Autowired
	private IUserRepo repo;
	
	@Autowired
	private ObjectMapper mapper;
	
	@Autowired
	private IImageService iService;
	
	@Autowired
	private IConverSationRepo cRepo;
	
	@Autowired
	private IChatMessageRepo cmRepo;
	
	
	@Override
	public ResponseEntity<?> addUser(String userRequest,MultipartFile profile) {
		// TODO Auto-generated method stub
		Map<String,Object> response = new HashMap<>();
		try {
			 User user = mapper.readValue(userRequest, User.class);
			 User u = repo.findUserByUsername(user.getUserName());
			 if(u!=null) 
			    {
			    	response.put(AppConstant.MESSAGE, AppConstant.USER_ALREADY_EXIST);
			    	return ResponseEntity.status(HttpStatus.OK).body(response);
			    }else 
			    {
			   	   user.setCreatedAt(LocalDateTime.now());
			   	   String image = iService.uploadImage(profile, AppConstant.PROFILE);
			   	   user.setProfile(image);
			   	   this.repo.save(user);
			   	   response.put(AppConstant.MESSAGE, AppConstant.USER_REGISTERED);
			   	   return ResponseEntity.status(HttpStatus.CREATED).body(response);
			    }
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		
	}

	

	@Override
	public ResponseEntity<?> loginUser(LoginRequest request) {
		// TODO Auto-generated method stub
		System.err.println(request);
		Map<String,Object> response = new HashMap<>();
		User u = this.repo.finduser(request.getUserName(),request.getPassword());
		if(u!=null) 
		{
			LoginResponse l = setDataInLoginResponse(u);
			response.put(AppConstant.RESPONSE, l);
			return ResponseEntity.status(HttpStatus.OK).body(response);
		}
		response.put(AppConstant.MESSAGE, AppConstant.USER_NOT_FOUND);
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
	}

	private LoginResponse setDataInLoginResponse(User u) {
		// TODO Auto-generated method stub
		LoginResponse l = new LoginResponse();
		l.setEmail(u.getEmail());
		l.setProfile(u.getProfile());
		l.setUserId(u.getUserId());
		l.setUserName(u.getUserName());
		return l;
	}

	
	@Override
	public ResponseEntity<?> reciverList(String userName) {
	    Map<String, Object> response = new HashMap<>();
	    List<User> users = repo.findUserByUsernameExcept(userName);
	    User sender = repo.findUserByUsername(userName);
	    List<ReceiverListResponse> receiverList = users.stream().map(user -> {
	    	Long receiverId= user.getUserId();
	    	Long senderId = sender.getUserId();
	    	Conversation conversation = cRepo.checkConverSation(receiverId, senderId);
	        ReceiverListResponse receiver = new ReceiverListResponse();
	        receiver.setCreatedAt(user.getCreatedAt());
	        receiver.setProfile(user.getProfile());
	        receiver.setUserName(user.getUserName());
	        receiver.setUserId(user.getUserId());
	        receiver.setEmail(user.getEmail());
	        if(conversation!=null) {
	        receiver.setConverSationId(conversation.getConversationId());
	        ChatMessage lastMessage = cmRepo.findLastMessageOfReceiverAndSender(receiver.getConverSationId());
	        if(lastMessage!=null) 
	        {
	        	receiver.setLatestMsg(lastMessage.getMessage());
	        	receiver.setLatestMsgTime(lastMessage.getCreatedAt());
	        }
	        }
	        return receiver;
	    }).collect(Collectors.toList());
	    response.put(AppConstant.RESPONSE, receiverList);
	    return ResponseEntity.status(HttpStatus.OK).body(response);
	}


	@Override
	public ResponseEntity<?> receiverContent(Long receiverId) {
		// TODO Auto-generated method stub
		Map<String,Object> response = new HashMap<>();
		User u = this.repo.findById(receiverId).get();
		response.put(AppConstant.RESPONSE, u);
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}



	@Override
	public ResponseEntity<?> searchReceiver(String key,String userName) {
		// TODO Auto-generated method stub
		Map<String,Object> response = new HashMap<>();
		
		List<User> userList = this.repo.searchReceiver(key,userName);
		User sender = repo.findUserByUsername(userName);
		
		 List<ReceiverListResponse> receiverList = userList.stream().map(user -> {
		    	Long receiverId= user.getUserId();
		    	Long senderId = sender.getUserId();
		    	Conversation conversation = cRepo.checkConverSation(receiverId, senderId);
		        ReceiverListResponse receiver = new ReceiverListResponse();
		        receiver.setCreatedAt(user.getCreatedAt());
		        receiver.setProfile(user.getProfile());
		        receiver.setUserName(user.getUserName());
		        receiver.setUserId(user.getUserId());
		        if(conversation!=null) {
		        receiver.setConverSationId(conversation.getConversationId());
		        ChatMessage lastMessage = cmRepo.findLastMessageOfReceiverAndSender(receiver.getConverSationId());
		        if(lastMessage!=null) 
		        {
		        	receiver.setLatestMsg(lastMessage.getMessage());
		        	receiver.setLatestMsgTime(lastMessage.getCreatedAt());
		        }
		        }
		        return receiver;
		    }).collect(Collectors.toList());
		 response.put(AppConstant.RESPONSE, receiverList);
		    return ResponseEntity.status(HttpStatus.OK).body(response);
	}



	@Override
	public ResponseEntity<?> isOnlineStatusUpdate(String email, Boolean status) {
		// TODO Auto-generated method stub
		User u = repo.findUserByEmail(email);
		Map<String,Object> response = new HashMap<>();
		if(u!=null) 
		{
			u.setIsOnline(status);
			if(status) {
			this.repo.save(u);
			}else 
			{
				u.setLastSeen(LocalDateTime.now());
				response.put(AppConstant.LAST_SEEN, u.getLastSeen());
				this.repo.save(u);	
			}
			response.put(AppConstant.MESSAGE, AppConstant.STATUS_UPDATE);
			return ResponseEntity.status(HttpStatus.OK).body(response);
		}
		response.put(AppConstant.MESSAGE, AppConstant.SOMETHING_WENT_WRONG);
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
	}



	@Override
	public OfflineUserWithLastSeen isOfflinegetLastSeen(String email) {
		// TODO Auto-generated method stub
		User u = repo.findUserByEmail(email);
		OfflineUserWithLastSeen offlineUserWithLastSeen = new OfflineUserWithLastSeen();
		offlineUserWithLastSeen.setEmail(email);
		offlineUserWithLastSeen.setLastSeen(u.getLastSeen());
		return offlineUserWithLastSeen;
	}



	@Override
	public List<ChatMessageStatusUpdateResponse> updateMessageStatus(String email) {
		// TODO Auto-generated method stub
		User u = repo.findUserByEmail(email);
		List<ChatMessage> findAllReceivedMessageOfReceiver = cmRepo.findAllReceivedMessageOfReceiver(u.getUserId(), MessageStatus.RECEIVED);
		List<ChatMessageStatusUpdateResponse> response = findAllReceivedMessageOfReceiver.stream().map(chatMessage->
		{
			ChatMessageStatusUpdateResponse cmsur = new ChatMessageStatusUpdateResponse();
			cmsur.setId(chatMessage.getId());
			cmsur.setReceiverId(chatMessage.getReceiver().getUserId());
			chatMessage.setStatus(MessageStatus.DELIVERD);
			cmsur.setStatus(MessageStatus.DELIVERD);
			cmsur.setMessage(chatMessage.getMessage());
			cmsur.setConversationId(chatMessage.getConversationId());
			return cmsur;
		}).collect(Collectors.toList());
		cmRepo.saveAll(findAllReceivedMessageOfReceiver);
        return response;
	}

}
