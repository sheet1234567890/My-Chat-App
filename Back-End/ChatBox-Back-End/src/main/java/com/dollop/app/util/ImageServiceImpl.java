package com.dollop.app.util;

import java.io.IOException;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
@Service
public class ImageServiceImpl implements IImageService {

	@Autowired
	public Cloudinary cloudinary;
	
	
	@Override
	public String uploadImage(MultipartFile file, String Dir) {
		// TODO Auto-generated method stub
		String originalFilename = StringUtils.cleanPath(file.getOriginalFilename());
		String uuid = UUID.randomUUID().toString();
		String fileType = originalFilename.substring(originalFilename.lastIndexOf("."));
		String randomName = uuid.concat(fileType);
		try {
			cloudinary.uploader().upload(file.getBytes(), ObjectUtils.asMap("public_id",Dir+"/"+randomName));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return Dir+"/"+randomName+fileType;
	}

}
