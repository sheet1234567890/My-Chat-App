package com.dollop.app.util;

import org.springframework.web.multipart.MultipartFile;

public interface IImageService {

	public String uploadImage(MultipartFile file,String Dir);
}
