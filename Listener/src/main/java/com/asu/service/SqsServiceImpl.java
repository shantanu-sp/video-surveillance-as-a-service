package com.asu.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.model.ReceiveMessageRequest;
import com.amazonaws.services.sqs.model.ReceiveMessageResult;

@Service
public class SqsServiceImpl implements SqsService{

	@Autowired
	private AmazonSQS awsSqs; 
        
    private static final Logger logger = LoggerFactory.getLogger(SqsServiceImpl.class);
    
//    @Autowired
//	public SqsServiceImpl(AmazonSQS sqsClient, String sqsQueueName) {
//		// TODO Auto-generated constructor stub
//		System.out.println("SQS Client Ready." + sqsQueueName);
//    	this.awsSqs = sqsClient;
//    	this.sqsQueueName = sqsQueueName;
//	}

    
//	public SqsServiceImpl(String queueName) {
//		// TODO Auto-generated constructor stub
//		System.out.println("SQS Client Ready." + queueName);
//    	//this.awsSqs = sqsClient;
//    	this.queueName = queueName;
//	}
    
	@Override
	public void sendMessage(String messageBody, String queueName) {
		// TODO Auto-generated method stub
		System.out.println("Sending message to queue : " + queueName);
		String sqsUrl = this.awsSqs.getQueueUrl(queueName).getQueueUrl();
		
		logger.info("Sending message to queue having url {}",sqsUrl);
		
//		SendMessageRequest sendMessageRequest = new SendMessageRequest().withQueueUrl(sqsUrl)
//				.withMessageBody(messageBody).withDelaySeconds(0);
		this.awsSqs.sendMessage(sqsUrl, messageBody);
		
	}

	@Override
	public Message receiveMessage(String queueName) {
		// TODO Auto-generated method stub
		System.out.println(queueName);
		String sqsUrl = this.awsSqs.getQueueUrl(queueName).getQueueUrl();
		ReceiveMessageResult result=this.awsSqs.receiveMessage(sqsUrl);
		List<Message> messages=result.getMessages();
		if (messages.isEmpty())
			return null;
		System.out.println("Length is :" + messages.size()) ;
		for(Message m : messages) {
			System.out.println(m.getBody());
		}
		return messages.get(0);
		
	}

	@Override
	public void deleteMessage(Message message, String queueName) {
		// TODO Auto-generated method stub
		String sqsUrl = this.awsSqs.getQueueUrl(queueName).getQueueUrl();
		String handle=message.getReceiptHandle();
		awsSqs.deleteMessage(sqsUrl, handle);
	}
	
	
	
	

}
