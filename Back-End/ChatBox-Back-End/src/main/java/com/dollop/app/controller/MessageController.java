package com.dollop.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dollop.app.service.IChatMessageService;

@RestController
@RequestMapping("/api/v1/msg")
public class MessageController {
	
	@Autowired
	private IChatMessageService service;


	@GetMapping("/{cId}")
	public ResponseEntity<?> c(@PathVariable String cId)
	{
		return service.lastMessage(cId);
	}
}
