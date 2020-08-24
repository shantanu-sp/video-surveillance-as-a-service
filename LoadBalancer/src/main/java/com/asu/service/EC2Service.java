package com.asu.service;

import com.amazonaws.services.ec2.model.DescribeInstanceStatusRequest;
import com.amazonaws.services.ec2.model.DescribeInstanceStatusResult;

public interface EC2Service {

	void stopInstance(String instanceId);

	void terminateInstance(String instanceId);

	DescribeInstanceStatusResult describeInstance(
			DescribeInstanceStatusRequest describeRequest);
	
	public Integer getTotalNumberOfInstances();

	void createInstance(int totalEc2ToLaunch);

}