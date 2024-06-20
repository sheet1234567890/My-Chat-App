package com.dollop.app.socket;

import org.springframework.stereotype.Service;

import com.corundumstudio.socketio.SocketIOClient;

@Service
public class SocketService {

	public void sendMessage(SocketIOClient client, Object message, String eventName) {
		System.err.println(eventName);
		client.sendEvent(eventName, message);
	}

}
