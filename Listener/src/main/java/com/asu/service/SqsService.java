package com.asu.service;

import com.amazonaws.services.sqs.model.Message;

public interface SqsService {
	
	public void sendMessage(String messageBody,String queueName);

	public Message receiveMessage(String queueName);
	
	public void deleteMessage(Message message,String queueName);

}
