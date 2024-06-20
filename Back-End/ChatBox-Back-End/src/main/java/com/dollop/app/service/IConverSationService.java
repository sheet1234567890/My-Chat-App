package com.dollop.app.service;

import org.springframework.http.ResponseEntity;

import com.dollop.app.payload.ConverSationRequest;

public interface IConverSationService {

	public String checkConversationId(ConverSationRequest request);
	public ResponseEntity<?> createConversation(ConverSationRequest request);
}
