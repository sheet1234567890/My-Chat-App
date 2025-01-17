package com.dollop.app.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.corundumstudio.socketio.SocketIOServer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
@Component
@RequiredArgsConstructor
@Slf4j
public class ServerCommandLineRunner implements CommandLineRunner {

	private final SocketIOServer server;
	@Override
	public void run(String... args) throws Exception {
		// TODO Auto-generated method stub
		server.start();

	}

}
