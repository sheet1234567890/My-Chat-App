package com.dollop.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dollop.app.payload.ConverSationRequest;
import com.dollop.app.service.IConverSationService;
@CrossOrigin
@RestController
@RequestMapping("/api/v1/conversion")
public class ConversationController {

	@Autowired
	private IConverSationService service;
	@PostMapping("/")
	public ResponseEntity<?> createConversation(@RequestBody ConverSationRequest request)
	{
		return service.createConversation(request);
	}
}
