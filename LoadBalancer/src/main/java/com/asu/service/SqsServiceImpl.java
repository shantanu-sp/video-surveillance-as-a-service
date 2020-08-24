package com.asu.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.regions.Region;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.amazonaws.services.sqs.model.GetQueueAttributesRequest;
import com.amazonaws.services.sqs.model.QueueDoesNotExistException;
import com.amazonaws.services.sqs.model.SendMessageRequest;

//@Component
//@ComponentScan("com.asu.")
@Service
public class SqsServiceImpl implements SqsService{

    public String getSqsQueueName() {
		return sqsQueueName;
	}

	public void setSqsQueueName(String sqsQueueName) {
		this.sqsQueueName = sqsQueueName;
	}

	private AmazonSQS awsSqs; 
    
    private String sqsQueueName;
    
    private static final Logger logger = LoggerFactory.getLogger(SqsServiceImpl.class);
	
//    @Autowired
//    public SqsServiceImpl(Region awsSqsRegion,String sqsQueueName,AWSCredentialsProvider awsCredentialsProvider) {
//    	
//    	// Un comment when migrate to AWS account.
//    	
//    	/*this.awsSqs = AmazonSQSClientBuilder.standard().
//    			withCredentials(awsCredentialsProvider).
//    			withRegion(awsRegion.getName()).
//    			build();
//    			*/
//    	
//    	//To pick up config locally.
//    	
//    	this.awsSqs = AmazonSQSClientBuilder.standard().build();
//    	logger.info("Built SQS queue connection");
//    	this.sqsQueueName = sqsQueueName;
//    }
    
    
    @Autowired
    public SqsServiceImpl(AmazonSQS sqsClient,String sqsQueueName) {
		// TODO Auto-generated constructor stub
    	System.out.println("SQS Client Ready.");
    	this.awsSqs = sqsClient;
    	this.sqsQueueName = sqsQueueName;
	}
    
	@Override
	public void sendMessage(String messageBody) {
		// TODO Auto-generated method stub
		
		String sqsUrl = this.awsSqs.getQueueUrl(this.sqsQueueName).getQueueUrl();
		
		logger.info("Sending message to queue having url {}",sqsUrl);
		
//		SendMessageRequest sendMessageRequest = new SendMessageRequest().withQueueUrl(sqsUrl)
//				.withMessageBody(messageBody).withDelaySeconds(0);
		this.awsSqs.sendMessage(sqsUrl, messageBody);
		
	}
	
	@Override
	public Integer getTotalNumberOfMessgaes(String QueueName) {
		logger.debug("Getting approximate number of messages.");
		String queueUrl = null;
		try {
			queueUrl = awsSqs.getQueueUrl(QueueName).getQueueUrl();
		} catch (QueueDoesNotExistException queueDoesNotExistException) {
			queueDoesNotExistException.printStackTrace();
		}
		queueUrl = awsSqs.getQueueUrl(QueueName).getQueueUrl();
	
		
		List<String> attributeNames = new ArrayList<String>();
		
		attributeNames.add("ApproximateNumberOfMessages");
		
		GetQueueAttributesRequest getQueueAttributesRequest = new GetQueueAttributesRequest(queueUrl, attributeNames);
		//HashMap<String, String> attributes= 
		Map<String, String> attribues = awsSqs.getQueueAttributes(getQueueAttributesRequest).getAttributes();
		String totalmessages = (String) attribues.get("ApproximateNumberOfMessages");
		Integer totalmessagesInteger = Integer.valueOf(totalmessages);
		if(totalmessagesInteger > 0)
			return totalmessagesInteger;
		return 0;
	}

}
