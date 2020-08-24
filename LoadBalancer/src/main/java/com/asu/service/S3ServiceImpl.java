package com.asu.service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.regions.Region;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.PutObjectRequest;

//@Component
//@ComponentScan("com.asu.")
@Service
public class S3ServiceImpl implements S3Service{

    private AmazonS3 amazonS3; 
    
    private String s3VideoBucketName;
    
    private String s3OutputBucketName;
    
    private static final Logger logger = LoggerFactory.getLogger(S3ServiceImpl.class);
    
//    @Autowired
//    public S3ServiceImpl(Region awsRegion, AWSCredentialsProvider awsCredentialsProvider, String awsS3AudioBucket) 
//    {
//        
//    	/*this.amazonS3 = AmazonS3ClientBuilder.standard()
//                .withCredentials(awsCredentialsProvider)
//                .withRegion(awsRegion.getName()).build();
//        
//        */
//    	// Use below line when running code locally.
//    	this.amazonS3 = AmazonS3ClientBuilder.standard().build();
//        this.s3BucketName = awsS3AudioBucket;
//    }
	
    
    @Autowired
    public S3ServiceImpl(AmazonS3 s3Client,String awsS3VideoBucket,String awsS3OutputBucket) {
    	System.out.println("S3 Client Ready.");
    	this.amazonS3 = s3Client;
    	this.s3VideoBucketName = awsS3VideoBucket;
    	this.s3OutputBucketName = awsS3OutputBucket;
    }
	
    public void uploadFileToS3OutputBucket(MultipartFile multiPartFile) {
    	uploadFileToS3(multiPartFile,this.s3OutputBucketName);
    }

    public void uploadFileToS3VideoBucket(MultipartFile multiPartFile) {
    	uploadFileToS3(multiPartFile,this.s3VideoBucketName);
    }
    
	@Override
	public void uploadFileToS3(MultipartFile multiPartFile, String bucket) {
		// TODO Auto-generated method stub
		// TODO Add utility which generates a unique file name (Add that).
        String rawFileName = multiPartFile.getOriginalFilename();
        try {
            
        	File file = new File(rawFileName);
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(multiPartFile.getBytes());
            fos.close();

            if(bucket.equals(this.s3OutputBucketName)) {
            	FileReader fr = null;
            	fr = new FileReader(file);
            	
            	BufferedReader br=new BufferedReader(fr);
				String s2="";
				String tmp = null;
				Boolean flag=false;
				
				while((tmp=br.readLine())!=null){
					String[] ts=tmp.split(":");
					
					if (ts[0].equals("FPS")) {
						flag=false;
					}
					
					if(flag==true) {
						if(ts.length>1) {
							if(s2.length()==0)
								s2+=ts[0];
							else
								if(!s2.contains(ts[0]))
									s2+=", "+ts[0];
						}
					}
					
					if(ts[0].equals("Objects")) {
						flag=true;
					}
				}
				
				br.close();
				fr.close();
				
				if(s2.length()==0)
					s2="No object detected";
				
				System.out.println(s2);
				
				FileWriter fw=null;
				BufferedWriter bw=null;
				
				fw=new FileWriter(file);
				bw=new BufferedWriter(fw);
				bw.write(s2);
				bw.close();
				fw.close();
            }
            
            PutObjectRequest putObjectRequest = new PutObjectRequest(bucket, rawFileName, file);

            this.amazonS3.putObject(putObjectRequest);
            //this.amazonS3.
            //TODO Delete files or not?
            file.delete();
        } catch (IOException | AmazonServiceException ex) {
            logger.error("error [" + ex.getMessage() + "] occurred while uploading [" + rawFileName + "] ");
        }
	}


}
