package com.asu.service;

public interface SqsService {
	
	public void sendMessage(String messageBody);
	
	public Integer getTotalNumberOfMessgaes(String QueueName);

}
