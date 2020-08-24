package com.asu.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.asu.model.AwsEnvironment;
import com.asu.service.S3ServiceImpl;
import com.asu.service.SqsServiceImpl;
import com.asu.service.TestService;


@RestController
@RequestMapping("/")

public class TestController {
	
	@Autowired TestService ts;

	@Autowired S3ServiceImpl awsS3;
	
	@Autowired SqsServiceImpl awsSqs;
	
    private static final Logger logger = LoggerFactory.getLogger(TestController.class);
    
	@GetMapping("/awsEnvironment")
	public AwsEnvironment getInfo() {
		System.out.println(ts.getEnv().toString());
		return ts.getEnv();
	}
	
	@PostMapping("/acceptVideoFile")
    public void uploadVideoFileToS3AndSqs(@RequestPart(value = "file1") MultipartFile videoFile)
    {
        //awsS3.uploadFileToS3(videoFile);
		awsS3.uploadFileToS3VideoBucket(videoFile);
        logger.info("Video File name to be pushed into SQS is : {}", videoFile.getOriginalFilename());
        awsSqs.sendMessage(videoFile.getOriginalFilename());
    }
	

	@PostMapping("/acceptVideoAndOpFile")
    public void uploadVideoAndOpToS3(@RequestPart(value = "file1") MultipartFile videoFile, 
    		@RequestPart(value = "file2") MultipartFile opFile)
    {
        //awsS3.uploadFileToS3(videoFile);
        //awsS3.uploadFileToS3(opFile);
		awsS3.uploadFileToS3VideoBucket(videoFile);
		awsS3.uploadFileToS3OutputBucket(opFile);
    }	
}