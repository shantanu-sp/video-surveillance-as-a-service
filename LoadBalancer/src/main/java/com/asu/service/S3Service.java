package com.asu.service;

import org.springframework.web.multipart.MultipartFile;

public interface S3Service {
	
	public void uploadFileToS3(MultipartFile file,String bucketName); 
	
	 
}
