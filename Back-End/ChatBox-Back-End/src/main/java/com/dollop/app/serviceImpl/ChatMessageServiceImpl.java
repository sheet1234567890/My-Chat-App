package com.dollop.app.serviceImpl;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.DataListener;
import com.dollop.app.data.User;
import com.dollop.app.data.chat.ChatMessage;
import com.dollop.app.data.chat.Conversation;
import com.dollop.app.data.chat.MessageStatus;
import com.dollop.app.payload.ChatMessageRequest;
import com.dollop.app.payload.ChatMessageRespnse;
import com.dollop.app.payload.ChatMessageStatusUpdateResponse;
import com.dollop.app.payload.ConverSationRequest;
import com.dollop.app.payload.FileSendingRequest;
import com.dollop.app.payload.LastMessageResponse;
import com.dollop.app.payload.OnlineRequest;
import com.dollop.app.payload.TypingRequest;
import com.dollop.app.repo.IChatMessageRepo;
import com.dollop.app.repo.IConverSationRepo;
import com.dollop.app.repo.IUserRepo;
import com.dollop.app.service.IChatMessageService;
import com.dollop.app.service.IConverSationService;
import com.dollop.app.socket.SocketModule;
import com.dollop.app.util.AppConstant;

import lombok.extern.slf4j.Slf4j;
@Service
@Slf4j
public class ChatMessageServiceImpl implements IChatMessageService {

	@Autowired
	private IConverSationRepo repo;
	
	@Autowired
	private IConverSationService service;
	
	@Autowired
	private IUserRepo uRepo;
	
	@Autowired
	private IChatMessageRepo mRepo;
	
	@Autowired
	private ModelMapper mapper;
	
	private SocketIOServer server;
	@Autowired
	private SocketModule module;
	
	public ChatMessageServiceImpl(SocketIOServer server) {
		super();
		this.server = server;
		this.server.addEventListener( AppConstant.SEND_MESSAGE_EVENT, Object.class, onChatReceived());
		this.server.addEventListener(AppConstant.TYPING, TypingRequest.class, typing());	
		this.server.addEventListener(AppConstant.SEEN_MSG_STATUS, Object.class, seenMsgUpdate());
	}

private DataListener seenMsgUpdate() {
		// TODO Auto-generated method stub
		return(senderClient,data,acksender)->
		{
			System.err.println("seen...msg");
		};
	}
private DataListener <TypingRequest>typing() {
		// TODO Auto-generated method stub
	return (senderClient,data,acksender)->
	{	
		acksender.sendAckData(AppConstant.TYPING_EVENT_ACK_DATA);
		module.sendMessage(data.getEmail(), data,  AppConstant.TYPING_EVENT);
	};	
	}
	private DataListener<Object> onChatReceived()
	{
		return (senderClient,data,ackSender)->{
			ackSender.sendAckData(AppConstant.SEND_MESSAGE_EVENT_ACK_DATA);
            ChatMessageRequest cr = mapper.map(data, ChatMessageRequest.class);
            this.sendMessage(cr);
		};
	}
	@Override
	public ResponseEntity<?> sendMessage(ChatMessageRequest request) {
		// TODO Auto-generated method stub
		Map<String,Object> response = new HashMap<>();
		ChatMessage c = SetDataInChatMessage(request);
		ChatMessage chatMessage = this.mRepo.save(c);
		ChatMessage message = mRepo.findById(chatMessage.getId()).get();
		ChatMessageRespnse cmr = setDataInChatMessageResponse(message);
		module.sendMessage(message.getSender().getEmail(), cmr, AppConstant.GET_MESSAGE_EVENT);
		module.sendMessage(message.getReceiver().getEmail(),cmr,AppConstant.GET_MESSAGE_EVENT);
		response.put(AppConstant.RESPONSE,"save");
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}

	private ChatMessageRespnse setDataInChatMessageResponse(ChatMessage message) {
		// TODO Auto-generated method stub
		ChatMessageRespnse cmr = new ChatMessageRespnse();
		cmr.setStatus(message.getStatus());
		cmr.setCreatedAt(message.getCreatedAt());
		cmr.setReceiverId(message.getReceiver().getUserId());
		cmr.setSenderId(message.getSender().getUserId());
		cmr.setMessage(message.getMessage());
		cmr.setId(message.getId());
		cmr.setConversationId(message.getConversationId());
		return cmr;
	}

	private ChatMessage SetDataInChatMessage(ChatMessageRequest request) {
		// TODO Auto-generated method stub
		ChatMessage c = new ChatMessage();
	    Conversation conversation =  repo.checkConverSation(request.getReceiverId(), request.getSenderId());
	    Optional<User> receiver =this.uRepo.findById(request.getReceiverId());
    	Optional<User> sender = this.uRepo.findById(request.getSenderId());
	    if(conversation!=null) 
	    {
	    	c.setConversationId(conversation.getConversationId());
	    	c.setCreatedAt(LocalDateTime.now());
	    	c.setMessage(request.getMessage());
	    	if(!receiver.get().getIsOnline()) {
	    	c.setStatus(MessageStatus.RECEIVED);
	    	}else 
	    	{
	    		c.setStatus(MessageStatus.DELIVERD);
	    	}
	    	c.setReceiver(receiver.get());
	    	c.setSender(sender.get());
	    	return c;
	    }
	    else 
	    {
	    	ConverSationRequest cr = new ConverSationRequest();
	    	cr.setReceiverId(request.getReceiverId());
	    	cr.setSenderId(request.getSenderId());
	        String cId=service.checkConversationId(cr);
	    	c.setConversationId(cId);
	    	c.setCreatedAt(LocalDateTime.now());
	    	c.setMessage(request.getMessage());
	    	if(!receiver.get().getIsOnline()) {
		    	c.setStatus(MessageStatus.RECEIVED);
		    	}else {
		    		c.setStatus(MessageStatus.DELIVERD);
		    	}
	    	c.setReceiver(receiver.get());
	    	c.setSender(sender.get());
	    }
		return c;
	}

	@Override
	public ResponseEntity<?> lastMessage(String cId) {
		// TODO Auto-generated method stub
		Map<String,Object> response = new HashMap<>();
		response.put(AppConstant.DATA,mRepo.findAllMsgByConversationId(cId));
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}
	
	
	

}

