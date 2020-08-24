package com.asu.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class AwsOpSqsConfig {
	
    @Value("${aws.sqs.opQueueName}")
    public String sqsQueueName;

    @Bean(name = "opSqsQueueName")
	public String getsqsQueueName() {
		return sqsQueueName;
	}
}