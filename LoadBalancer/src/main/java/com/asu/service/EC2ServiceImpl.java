package com.asu.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.regions.Region;
import com.amazonaws.services.ec2.AmazonEC2;
import com.amazonaws.services.ec2.AmazonEC2ClientBuilder;
import com.amazonaws.services.ec2.model.DescribeInstanceStatusRequest;
import com.amazonaws.services.ec2.model.DescribeInstanceStatusResult;
import com.amazonaws.services.ec2.model.Instance;
import com.amazonaws.services.ec2.model.InstanceState;
import com.amazonaws.services.ec2.model.InstanceStateName;
import com.amazonaws.services.ec2.model.InstanceStatus;
import com.amazonaws.services.ec2.model.RunInstancesRequest;
import com.amazonaws.services.ec2.model.RunInstancesResult;
import com.amazonaws.services.ec2.model.StartInstancesRequest;
import com.amazonaws.services.ec2.model.StopInstancesRequest;
import com.amazonaws.services.ec2.model.Tag;
import com.amazonaws.services.ec2.model.TagSpecification;
import com.amazonaws.services.ec2.model.TerminateInstancesRequest;

//@Component
@Service
public class EC2ServiceImpl implements EC2Service{

    private AmazonEC2 amazonEC2; 
    
    private static final Logger logger = LoggerFactory.getLogger(EC2ServiceImpl.class);

//    @Autowired
//    public EC2ServiceImpl(Region awsSqsRegion,AWSCredentialsProvider awsCredentialsProvider) 
//    {
//    	/*AmazonEC2 amazonEC2 = AmazonEC2ClientBuilder.standard().withRegion(awsRegion.getName())
//				.withCredentials(awsCredentialsProvider).build();
//		*/
//    	
//    	this.amazonEC2 = AmazonEC2ClientBuilder.standard().build();  	
//    }
    
    //private static final String IMAGEID = "ami-0903fd482d7208724";
    private static final String IMAGEID = "ami-0e57108c3d56f51b3";

    
    @Autowired
    public EC2ServiceImpl(AmazonEC2 ec2Client) 
    {
    	/*AmazonEC2 amazonEC2 = AmazonEC2ClientBuilder.standard().withRegion(awsRegion.getName())
				.withCredentials(awsCredentialsProvider).build();
		*/
    	System.out.println("EC2 Client Ready.");
    	this.amazonEC2 = ec2Client;  	
    }
    
	@Override
	public void stopInstance(String instanceId) {
		// TODO Auto-generated method stub
		logger.debug("Stopping the instance.");
		StopInstancesRequest request = new StopInstancesRequest().withInstanceIds(instanceId);
		amazonEC2.stopInstances(request);		
	}

	@Override
	public void terminateInstance(String instanceId) {
		// TODO Auto-generated method stub
		logger.debug("Terminating the instance.");
		TerminateInstancesRequest request = new TerminateInstancesRequest().withInstanceIds(instanceId);
		amazonEC2.terminateInstances(request);		
	}

	@Override
	public DescribeInstanceStatusResult describeInstance(DescribeInstanceStatusRequest describeRequest) {
		// TODO Auto-generated method stub
		return amazonEC2.describeInstanceStatus(describeRequest);
	}

	@Override
	public Integer getTotalNumberOfInstances() {
		
		DescribeInstanceStatusRequest dr = new DescribeInstanceStatusRequest();
		dr.setIncludeAllInstances(true);
		
		DescribeInstanceStatusResult drResult = amazonEC2.describeInstanceStatus(dr);
		List<InstanceStatus> instanceList = drResult.getInstanceStatuses();
		Integer totalInstances = 0;
		for (InstanceStatus iS : instanceList) {
			InstanceState instanceState = iS.getInstanceState();
			if (instanceState.getName().equals(InstanceStateName.Running.toString()) || instanceState.getName().equals(InstanceStateName.Pending.toString())) {
					totalInstances++;
			}
		}
		
		return totalInstances;
	}
	/*	@Override
	public Integer getTotalNumberOfInstances() {
		
		DescribeInstanceStatusRequest dr = new DescribeInstanceStatusRequest();
		dr.setIncludeAllInstances(true);
		
		DescribeInstanceStatusResult drResult = amazonEC2.describeInstanceStatus(dr);
		List<InstanceStatus> instanceList = drResult.getInstanceStatuses();
		Integer totalInstances = 0;
		for (InstanceStatus iS : instanceList) {
			InstanceState instanceState = iS.getInstanceState();
			if (instanceState.getName().equals(InstanceStateName.Running.toString())) {
					totalInstances++;
			}
		}
		
		return totalInstances;
	}*/
	
/*	@Override
	public Integer getTotalNumberOfInstances() {
		// TODO Auto-generated method stub
		DescribeInstanceStatusRequest dr = new DescribeInstanceStatusRequest();
		dr.setIncludeAllInstances(true);
		
		DescribeInstanceStatusResult drResult = amazonEC2.describeInstanceStatus(dr);
		List<InstanceStatus> instanceList = drResult.getInstanceStatuses();
		Integer countOfRunningInstances = 0;
		for (InstanceStatus iS : instanceList) {
			InstanceState instanceState = iS.getInstanceState();
			if (instanceState.getName().equals(InstanceStateName.Running.toString())) {
				countOfRunningInstances++;
			}
		}
		
		return countOfRunningInstances;
	}*/

/*	@Override
	public void createInstance(int totalEc2ToLaunch) {
		// TODO Auto-generated method stub
		logger.debug("Creating the instance.");
		
		int minInstanceCount = totalEc2ToLaunch - 1; // create 1 instance
		int maxInstanceCount = totalEc2ToLaunch;
		if(minInstanceCount == 0)
			minInstanceCount = 1;
	
		Collection<TagSpecification> tagSpecifications = new ArrayList<TagSpecification>();
		TagSpecification tagSpecification = new TagSpecification();
		Collection<Tag> tags = new ArrayList<Tag>();
		Tag t = new Tag();
		t.setKey("Name");
		t.setValue("App-Instance");
		tags.add(t);
		tagSpecification.setResourceType("instance");
		tagSpecification.setTags(tags);
		tagSpecifications.add(tagSpecification);
		
		RunInstancesRequest rir = new RunInstancesRequest("ami-0903fd482d7208724", minInstanceCount, maxInstanceCount);
		rir.setInstanceType("t2.micro");
	
		rir.setTagSpecifications(tagSpecifications);
		
		RunInstancesResult result = null;
		try {
			result = amazonEC2.runInstances(rir);
		} catch (Exception e) {
			return ;
		}
		Instance instance = result.getReservation().getInstances().get(0);
        String instanceId = instance.getInstanceId();
        System.out.println("EC2 Instance Id: " + instanceId);
        
        StartInstancesRequest startInstancesRequest = new StartInstancesRequest().withInstanceIds(instanceId);
        
        amazonEC2.startInstances(startInstancesRequest);
		
	}*/

	
/*	@Override
	public void createInstance(int totalEc2ToLaunch) {		
		int minCount = totalEc2ToLaunch - 1; 
		
		int maxCount = totalEc2ToLaunch;
		if(minCount == 0)
			minCount = 1;
	
		Collection<TagSpecification> tagSpecifications = new ArrayList<TagSpecification>();
		TagSpecification ts = new TagSpecification();
		Collection<Tag> tags = new ArrayList<Tag>();
		Tag t = new Tag();
		t.setKey("Name");
		t.setValue("App-Tier");
		
		tags.add(t);
		
		ts.setResourceType("instance");
		ts.setTags(tags);
		
		tagSpecifications.add(ts);
		
		//RunInstancesRequest request = new RunInstancesRequest("ami-0903fd482d7208724", minCount, maxCount);
		//ami-0587f285c08a73a39
		RunInstancesRequest request = new RunInstancesRequest("ami-0587f285c08a73a39", minCount, maxCount);
		
		request.setTagSpecifications(tagSpecifications);
		request.setInstanceType("t2.micro");
		RunInstancesResult result = null;
		try {
			result = amazonEC2.runInstances(request);
		} catch (Exception e) {
			return ;
		}
	
	
	
		
	}*/
	
	@Override
	public void createInstance(int totalEc2ToLaunch) {		
		int minCount = totalEc2ToLaunch - 1; 
		
		int maxCount = totalEc2ToLaunch;
		if(minCount == 0)
			minCount = 1;
	
		Collection<TagSpecification> tagSpecifications = new ArrayList<TagSpecification>();
		TagSpecification ts = new TagSpecification();
		Collection<Tag> tags = new ArrayList<Tag>();
		Tag t = new Tag();
		t.setKey("Name");
		t.setValue("App-Tier");
		
		tags.add(t);
		ts.setResourceType("instance");
		ts.setTags(tags);
		
		tagSpecifications.add(ts);
		
		RunInstancesRequest request = new RunInstancesRequest(IMAGEID, totalEc2ToLaunch, totalEc2ToLaunch);
		
		request.setTagSpecifications(tagSpecifications);
		request.setInstanceType("t2.micro");
		try {
			amazonEC2.runInstances(request);
		} catch (Exception e) {
			return ;
		}
		
	}
	
	
}
