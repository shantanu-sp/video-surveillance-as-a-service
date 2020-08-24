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
public class AwsS3Config {
	
    @Value("${aws.s3.video.bucket}")
    public String awsS3VideoBucket;

    @Value("${aws.s3.output.bucket}")
    public String awsS3OutputBucket;
    
    @Bean(name = "awsS3VideoBucket")
	public String getS3Bucket() {
		return awsS3VideoBucket;
	}    

    @Bean(name= "awsS3OutputBucket")
    public String getOutputS3Bucket() {
    	return awsS3OutputBucket;
    }
}

/*
@Configuration
public class AwsS3Config {
	
	@Autowired
	AwsEnvironment env;

    @Value("${aws.s3.audio.bucket}")
    private String awsS3AudioBucket;
    
//    private String awsS3AudioBucket = "cloudproject1203";

	
    @Bean(name = "awsRegion")
	public Region getS3Region() {
		return Region.getRegion(Regions.fromName(env.getRegion()));
	}

    @Bean(name = "awsS3AudioBucket")
	public String getS3Bucket() {
		return awsS3AudioBucket;
	}
	
    @Bean(name = "awsCredentialsProvider")
    public AWSCredentialsProvider getAWSCredentials() {
        BasicAWSCredentials awsCredentials = 
        		new BasicAWSCredentials(env.getAws_access_key_id(), env.getAws_secret_access_key());
        
        System.out.println("Credentials ready!!");
        return new AWSStaticCredentialsProvider(awsCredentials);
    }

}*/
