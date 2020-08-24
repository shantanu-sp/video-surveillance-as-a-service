package com.asu.service;

import java.io.File;

public interface S3Service {
	
	public void uploadFileToS3(File file,String bucket,String bucketKey); 
	
	public File getFileFromS3(String key);
	 
}
