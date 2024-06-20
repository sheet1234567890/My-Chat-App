package com.dollop.app.serviceImpl;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


import com.dollop.app.data.User;
import com.dollop.app.data.chat.ChatMessage;
import com.dollop.app.data.chat.Conversation;
import com.dollop.app.payload.ChatMessageRespnse;
import com.dollop.app.payload.ConverSationRequest;
import com.dollop.app.repo.IChatMessageRepo;
import com.dollop.app.repo.IConverSationRepo;
import com.dollop.app.repo.IUserRepo;
import com.dollop.app.service.IConverSationService;
import com.dollop.app.util.AppConstant;
@Service
public class ConverSationServiceImpl implements IConverSationService{

	@Autowired
	private IUserRepo uRepo;
	
	@Autowired
	private IConverSationRepo cRepo;
	
	@Autowired
	private IChatMessageRepo mRepo;
	
	
	@Override
	public String checkConversationId (ConverSationRequest request) {
		// TODO Auto-generated method stub
		Conversation c = this.cRepo.checkConverSation(request.getReceiverId(),request.getSenderId());
		if(c==null) 
		{
		   c=setDataInConverSation(request);
		   Conversation c1 = setDataInConversationSenterToReceiver(request);
		   c1.setConversationId(c.getConversationId());
		   cRepo.save(c);
		   cRepo.save(c1);
		   return c.getConversationId();
		}
		else 
		{
			return c.getConversationId();
		}
		
	}

	private Conversation setDataInConversationSenterToReceiver(ConverSationRequest request) {
		// TODO Auto-generated method stub
		 Conversation c = new Conversation();
		 c.setCreatedAt(LocalDateTime.now());
	     Optional<User>recepeint = this.uRepo.findById(request.getReceiverId());
	     Optional<User>sender = this.uRepo.findById(request.getSenderId());
	     c.setRecipientId(sender.get());
	     c.setSenderId(recepeint.get());
		return c;
		
	}

	private Conversation setDataInConverSation(ConverSationRequest request) {
		// TODO Auto-generated method stub
		 Conversation c = new Conversation();
		 c.setConversationId(UUID.randomUUID().toString());
		 c.setCreatedAt(LocalDateTime.now());
	     Optional<User>recepeint = this.uRepo.findById(request.getReceiverId());
	     Optional<User>sender = this.uRepo.findById(request.getSenderId());
	     c.setRecipientId(recepeint.get());
	     c.setSenderId(sender.get());
		return c;
	}

	

	@Override
	public ResponseEntity<?> createConversation(ConverSationRequest request) {
		// TODO Auto-generated method stub
		Map<String,Object> response = new HashMap<>();
		Conversation c = this.cRepo.checkConverSation(request.getReceiverId(),request.getSenderId());
		User receiver = this.uRepo.findById(request.getReceiverId()).get();
		if(c==null) 
		{
		   c=setDataInConverSation(request);
		   Conversation c1 = setDataInConversationSenterToReceiver(request);
		   c1.setConversationId(c.getConversationId());
		   cRepo.save(c);
		   cRepo.save(c1);
		   response.put(AppConstant.RECEIVER_EMAIL, receiver.getEmail());
		   response.put(AppConstant.CONVERSATION_ID, c.getConversationId());
		   response.put(AppConstant.RECEIVER_NAME, receiver.getUserName());
		   response.put(AppConstant.IMAGE,receiver.getProfile());
		   if(!receiver.getIsOnline()) {
		   response.put(AppConstant.LAST_SEEN, receiver.getLastSeen());
		   }
		   return ResponseEntity.status(HttpStatus.OK).body(response);
		}
		else 
		{
			 List<ChatMessage> chatMsgList = this.mRepo.findAllMsgByConversationId(c.getConversationId());
		       List<ChatMessageRespnse> chatMessageResponse =  chatMsgList.stream().map(cmr->
		       {
		    	  ChatMessageRespnse ch = new ChatMessageRespnse();
		    	  ch.setCreatedAt(cmr.getCreatedAt());
		    	  ch.setMessage(cmr.getMessage());
		    	  ch.setReceiverId(cmr.getReceiver().getUserId());
		    	  ch.setSenderId(cmr.getSender().getUserId());
		    	  ch.setStatus(cmr.getStatus());
		    	  ch.setId(cmr.getId());
		    	 
		    	  return ch;
		       }).collect(Collectors.toList());  
		     response.put(AppConstant.IMAGE,receiver.getProfile());
		     response.put(AppConstant.RECEIVER_EMAIL, receiver.getEmail());
			 response.put(AppConstant.RECEIVER_NAME, receiver.getUserName());
			 response.put(AppConstant.DATA, chatMessageResponse);
			 response.put(AppConstant.CONVERSATION_ID, c.getConversationId());
			 System.err.println(receiver.getIsOnline());
			 if(!receiver.getIsOnline()) {
				   response.put(AppConstant.LAST_SEEN, receiver.getLastSeen());
				   }
			 return ResponseEntity.status(HttpStatus.OK).body(response);
		}
	}

}
