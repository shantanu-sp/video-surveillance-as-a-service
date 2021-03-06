package com.asu.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.asu.model.AwsEnvironment;

@Component
public class AwsSqsConfig {
	
    @Value("${aws.sqs.name}")
    public String sqsQueueName;

    @Bean(name = "sqsQueueName")
	public String getsqsQueueName() {
		return sqsQueueName;
	}
}

/*
@Configuration
public class AwsSqsConfig {
	
	@Autowired
	AwsEnvironment env;
	
    @Value("${aws.sqs.name}")
    public String sqsQueueName;

//    private String sqsQueueName = "LoadBalancerQueue";
    
    @Bean(name = "sqsQueueName")
	public String getsqsQueueName() {
		return sqsQueueName;
	}

    @Bean(name = "awsSqsRegion")
	public Region getSQSRegion() {
		return Region.getRegion(Regions.fromName(env.getRegion()));
	}
    
//    @Bean(name = "awsCredentialsProvider")
//    public AWSCredentialsProvider getAWSCredentials() {
//        BasicAWSCredentials awsCredentials = 
//        		new BasicAWSCredentials(env.getAws_access_key_id(), env.getAws_secret_access_key());
//        
//        System.out.println("SQS Credentials ready!!");
//        return new AWSStaticCredentialsProvider(awsCredentials);
//    }    
}
*/