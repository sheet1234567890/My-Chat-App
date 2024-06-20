package com.dollop.app.socket;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.ConnectListener;
import com.corundumstudio.socketio.listener.DisconnectListener;
import com.dollop.app.payload.ChatMessageStatusUpdateResponse;
import com.dollop.app.payload.OfflineUserWithLastSeen;
import com.dollop.app.service.IUserService;
import com.dollop.app.util.AppConstant;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class SocketModule {

	private final SocketIOServer server;
	private final SocketService service;
	@Autowired
	private IUserService userService;
	private Map<String, SocketIOClient> clientMap = new HashMap<>();
	private Set<String> onlineUsers = Collections.synchronizedSet(new HashSet<>());

	public SocketModule(SocketIOServer server, SocketService service) {
		super();
		this.server = server;
		this.service = service;
		this.server.addConnectListener(onConnected());
		this.server.addDisconnectListener(onDisconnected());
	}

	private ConnectListener onConnected() {
		System.err.println("connected");
		return (client) -> {
			String email = client.getHandshakeData().getSingleUrlParam("email");
			clientMap.put(email, client);
			userService.isOnlineStatusUpdate(email, true);
			onlineUsers.add(email);
			broadcastOnlineUsers();
			List<ChatMessageStatusUpdateResponse> updateMessageStatus = userService.updateMessageStatus(email);
	         broadcastMessageStatusUpdate(updateMessageStatus);
			log.info("Socket IB[{}] Connected to socket", client.getSessionId().toString());
		};
	}
    

	private DisconnectListener onDisconnected() {
		return client -> {
			String email = client.getHandshakeData().getSingleUrlParam("email");
			userService.isOnlineStatusUpdate(email, false);
			onlineUsers.remove(email);
			OfflineUserWithLastSeen offlinegetLastSeen = userService.isOfflinegetLastSeen(email);
			broadcastOnlineUsers();
			broadcastOfflineUser(offlinegetLastSeen);
			log.info("Client[{}] - Disconnected from socket", client.getSessionId().toString());
		};
	}
	

	private void broadcastMessageStatusUpdate(List<ChatMessageStatusUpdateResponse> updateMessageStatus) {
		// TODO Auto-generated method stub
		server.getBroadcastOperations().sendEvent(AppConstant.UPDATE_MSG_STATUS, updateMessageStatus);
	}

	private void broadcastOfflineUser(OfflineUserWithLastSeen offlinegetLastSeen) {
		// TODO Auto-generated method stub
		server.getBroadcastOperations().sendEvent(AppConstant.OFFLINE_EVENT, offlinegetLastSeen);
	}

	private void broadcastOnlineUsers() {
		server.getBroadcastOperations().sendEvent(AppConstant.ONLINE_EVENT, onlineUsers);
	}

	public void sendMessage(String email, Object message, String eventName) {
		if (Objects.nonNull(getValueFromMap(email)))
			service.sendMessage(getValueFromMap(email), message, eventName);
	}

	public SocketIOClient getValueFromMap(String email) {
		return clientMap.get(email);
	}

}
