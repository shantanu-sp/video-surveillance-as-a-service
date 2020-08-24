package com.asu.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amazonaws.services.ec2.AmazonEC2;
import com.amazonaws.services.ec2.model.TerminateInstancesRequest;
import com.amazonaws.util.EC2MetadataUtils;

@Service
public class Ec2ServiceImpl implements Ec2Service {
	
	private AmazonEC2 amazonEC2;
	
	@Autowired
    public void EC2ServiceImpl(AmazonEC2 ec2Client) 
    {
    	/*AmazonEC2 amazonEC2 = AmazonEC2ClientBuilder.standard().withRegion(awsRegion.getName())
				.withCredentials(awsCredentialsProvider).build();
		*/
    	System.out.println("EC2 Client Ready.");
    	this.amazonEC2 = ec2Client;	
    }
	
	@Override
	public void endInstace() {
		// TODO Auto-generated method stub
		TerminateInstancesRequest terminateInstancesRequest=new TerminateInstancesRequest().withInstanceIds(EC2MetadataUtils.getInstanceId());
		amazonEC2.terminateInstances(terminateInstancesRequest);
	}

}
