package com.asu.model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


@Component
public class AwsEnvironment {
	@Override
	public String toString() {
		return "AwsEnvironment [aws_access_key_id=" + aws_access_key_id + ", aws_secret_access_key="
				+ aws_secret_access_key + ", region=" + region
				+ ", format=" + format + "]";
	}


	private String aws_access_key_id;
	private String aws_secret_access_key;
	private String region;
	private String format;
	
    @Autowired
    public AwsEnvironment(@Value("${app.aws_access_key_id}") String aws_access_key_id, 
    		@Value("${app.aws_secret_access_key}") String aws_secret_access_key,
    		@Value("${app.region}") String region, @Value("${app.format}") String format) {
        this.aws_access_key_id = aws_access_key_id;
        this.aws_secret_access_key = aws_secret_access_key;
        this.format = format;
        this.region = region;
    }

//    @Autowired
//    public AwsEnvironment() {
//        this.aws_access_key_id = "3123123123";
//        this.aws_secret_access_key = "213123123";
//        this.aws_session_token = "123123133";
//        this.format = "json";
//        this.region = "us-east-1";
//    }

    public String getAws_access_key_id() {
		return aws_access_key_id;
	}


	public void setAws_access_key_id(String aws_access_key_id) {
		this.aws_access_key_id = aws_access_key_id;
	}


	public String getAws_secret_access_key() {
		return aws_secret_access_key;
	}


	public void setAws_secret_access_key(String aws_secret_access_key) {
		this.aws_secret_access_key = aws_secret_access_key;
	}


	public String getRegion() {
		return region;
	}


	public void setRegion(String region) {
		this.region = region;
	}


	public String getFormat() {
		return format;
	}


	public void setFormat(String format) {
		this.format = format;
	}	
}