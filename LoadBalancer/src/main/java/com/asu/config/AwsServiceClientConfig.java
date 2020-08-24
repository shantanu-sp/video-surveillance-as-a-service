package com.asu.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.ec2.AmazonEC2;
import com.amazonaws.services.ec2.AmazonEC2ClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.asu.model.AwsEnvironment;
import com.asu.service.AutoScaling;
import com.asu.service.AutoScalingImpl;
import com.asu.service.S3Service;
import com.asu.service.S3ServiceImpl;
import com.asu.service.SqsService;
import com.asu.service.SqsServiceImpl;

@ComponentScan("com.asu")
@Configuration
public class AwsServiceClientConfig {
	
	@Autowired AwsEnvironment env;
	
	@Autowired AwsSqsConfig sqsConf;
	
	@Autowired AwsS3Config s3Conf;
	
	@Bean
	public AWSCredentialsProvider getAWSCredentials() {
		BasicAWSCredentials awsCredentials = new BasicAWSCredentials(env.getAws_access_key_id(),env.getAws_secret_access_key());
		System.out.println("Credentials ready in Aws Service Config!!");
	    return new AWSStaticCredentialsProvider(awsCredentials);
	}
	
	@Bean(name = "sqsClient")
	public AmazonSQS getSQSClient() {
		AmazonSQS awsSqs = AmazonSQSClientBuilder.standard().
    			withCredentials(getAWSCredentials()).
    			withRegion(env.getRegion()).
    			build();
//		AmazonSQS awsSqs = AmazonSQSClientBuilder.standard().build();
//		logger.info("Built SQS queue connection");
		return awsSqs;
	}
	
	@Bean(name = "ec2Client")
	public AmazonEC2 amazonEC2Client() {
		AmazonEC2 amazonEC2 = AmazonEC2ClientBuilder.standard().withRegion(env.getRegion())
				.withCredentials(getAWSCredentials()).build();
//		AmazonEC2 amazonEC2 = AmazonEC2ClientBuilder.standard().build();
		return amazonEC2;
	}
	
	@Bean(name = "s3Client")
	public AmazonS3 amazonS3Client() {

		AmazonS3 amazonS3  = AmazonS3ClientBuilder.standard()
        .withCredentials(getAWSCredentials())
        .withRegion(env.getRegion()).build();


    	// Use below line when running code locally.
//    	AmazonS3 amazonS3 = AmazonS3ClientBuilder.standard().build();
    	return amazonS3;
	}
	
//	@Bean()
//	public AutoScaling getImpl() {
//		return new AutoScalingImpl();
//	}
	
	@Bean()
	public SqsService getSqsService() {
		return new SqsServiceImpl(getSQSClient(), sqsConf.sqsQueueName);
	}
	
	@Bean
	public S3Service getS3Service() {
		return new S3ServiceImpl(amazonS3Client(), s3Conf.awsS3VideoBucket,s3Conf.awsS3OutputBucket);
	}
	}