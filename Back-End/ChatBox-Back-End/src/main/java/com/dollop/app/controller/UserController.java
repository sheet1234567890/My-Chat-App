package com.dollop.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.dollop.app.payload.LoginRequest;
import com.dollop.app.payload.UserRequest;
import com.dollop.app.service.IUserService;

@RestController
@RequestMapping("/api/v1/user")
@CrossOrigin("*")
public class UserController {

	@Autowired
	private IUserService service;
	
	@PostMapping("/save")
	public ResponseEntity<?> addUser( String userRequest ,MultipartFile profile)
	{
		return service.addUser(userRequest,profile);
	}
	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody LoginRequest request)
	{
		return service.loginUser(request);
	}
	@GetMapping("/list/{userName}")
	public ResponseEntity<?> receiverList(@PathVariable String userName)
	{
		return service.reciverList(userName);
	}
	@GetMapping("/{receiverId}")
	public ResponseEntity<?> receiverContent(@PathVariable Long receiverId)
	{
		return service.receiverContent(receiverId);
	}
	@GetMapping("/search/{key}/{userName}")
	public ResponseEntity<?> searchReceiver(@PathVariable String key,@PathVariable String userName)
	{
		return service.searchReceiver(key,userName);
	}
	@GetMapping("/status/{email}/{status}")
	public ResponseEntity<?> updateStatus(@PathVariable String email,@PathVariable Boolean status)
	{
		System.err.println("status");
		return service.isOnlineStatusUpdate(email, status);
	}
}
