package com.dollop.app.service;

import org.springframework.http.ResponseEntity;

import com.dollop.app.payload.FileSendingRequest;

public interface IFileSendingService {

	 ResponseEntity<?> saveFile(FileSendingRequest fileSendingRequest);
}
