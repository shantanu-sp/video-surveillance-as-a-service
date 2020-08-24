package com.asu.service;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;

@Service
public class S3ServiceImpl implements S3Service{

    private AmazonS3 amazonS3; 
    
    private String s3VideoBucketName;

    private String s3OutputBucketName;
    
    private static final Logger logger = LoggerFactory.getLogger(S3ServiceImpl.class);
    
    @Autowired
    public S3ServiceImpl(AmazonS3 s3Client,String awsS3VideoBucket,String awsS3OutputBucket) {
    	System.out.println("S3 Client Ready.");
    	this.amazonS3 = s3Client;
    	this.s3VideoBucketName = awsS3VideoBucket;
    	this.s3OutputBucketName = awsS3OutputBucket;    	
    }

    public void uploadFileToS3OutputBucket(File file) {
    	uploadFileToS3(file,this.s3OutputBucketName,file.getName()+".h264");
    }

    public void uploadFileToS3VideoBucket(File file) {
    	uploadFileToS3(file,this.s3VideoBucketName,file.getName());
    }    
	
	@Override
	public void uploadFileToS3(File file,String bucket,String S3key) {
		// TODO Auto-generated method stub
		//TODO use filename with or without extension

		String rawFileName = file.getName();
        try {

            PutObjectRequest putObjectRequest = new PutObjectRequest(bucket, S3key, file);

            this.amazonS3.putObject(putObjectRequest);
            //TODO Delete files or not?
            file.delete();
        } catch (AmazonServiceException ex) {
            logger.error("error [" + ex.getMessage() + "] occurred while uploading [" + rawFileName + "] ");
        }
	}


	@Override
	public File getFileFromS3(String key) {
		// TODO Auto-generated method stub
		
		GetObjectRequest getObjectRequest = new GetObjectRequest(this.s3VideoBucketName, key);
		
		File destinationFile = new File(key);
		
		this.amazonS3.getObject(getObjectRequest, destinationFile);
		
		return destinationFile;
	}

	

}
