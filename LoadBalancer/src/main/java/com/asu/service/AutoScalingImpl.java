package com.asu.service;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

//@ComponentScan("com.asu.")
@Service
public class AutoScalingImpl implements AutoScaling{

	@Autowired SqsServiceImpl awsSqs;
	
	@Autowired EC2ServiceImpl awsEC2;
	
	private static final int MAXIMUM_INSTANCES = 19;
	
	@Override
	public void LaunchInstances() {
		
		while(true){
			 int inputQueueTotalMessage= awsSqs.getTotalNumberOfMessgaes(awsSqs.getSqsQueueName());
		     int ec2InstancesTotal= awsEC2.getTotalNumberOfInstances();
		     
		     ec2InstancesTotal = ec2InstancesTotal-1; 
		     
		     if(inputQueueTotalMessage > ec2InstancesTotal){
		    	 int totalEc2ToLaunch = inputQueueTotalMessage - ec2InstancesTotal ;
		    	 totalEc2ToLaunch = Math.min(MAXIMUM_INSTANCES, totalEc2ToLaunch);
		    	 
			     System.out.println("QueueTotal:"+inputQueueTotalMessage+" ec2Instance:"+ ec2InstancesTotal+" totalEc2ToLaunch:"+totalEc2ToLaunch);
			     System.out.println(" ");
			     awsEC2.createInstance(totalEc2ToLaunch);			     
		     }
		     try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}	     
		}
		
	}
	
/*	 @Override
	 public void LaunchInstances() {
		
		while(true){
			 Integer inputQueueTotalMessage= this.awsSqs.getTotalNumberOfMessgaes(awsSqs.getSqsQueueName());
		     Integer ec2InstancesTotal= awsEC2.getTotalNumberOfInstances();
		     
		     System.out.println(" ");
		     int totalEc2ToLaunch = inputQueueTotalMessage - ec2InstancesTotal +1 ;
		     System.out.println("QueueTotal:"+inputQueueTotalMessage+" ec2Instance:"+ ec2InstancesTotal+" totalEc2ToLaunch:"+totalEc2ToLaunch);
		     
		     System.out.println(" ");
		     if(totalEc2ToLaunch > 0){
		    	 awsEC2.createInstance(totalEc2ToLaunch);
		    	 
		     }
		     try {
				Thread.sleep(45000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}	     
		}
	 }*/
	
	//@PostConstruct
	/*
	@Override
	public void LaunchInstances() {
		System.out.println("Here somehow!!");
		// TODO Auto-generated method stub
		while(true){
			 System.out.println("Autoscale!!");
			 Integer inputQueueTotalMessage= this.awsSqs.getTotalNumberOfMessgaes("LoadBalancerQueue");
		     Integer ec2InstancesTotal= this.awsEC2.getTotalNumberOfInstances();
		     System.out.println("queue size "+inputQueueTotalMessage);
		     System.out.println("No of instances " + ec2InstancesTotal);
		     //int totalEc2ToLaunch = inputQueueTotalMessage - ec2InstancesTotal -1;
		     int totalEc2ToLaunch = inputQueueTotalMessage - (ec2InstancesTotal -1);
		     if(totalEc2ToLaunch > 0){
		    	 this.awsEC2.createInstance(totalEc2ToLaunch);
		    	 
		     }
		     try {
				Thread.sleep(30000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}	     
		}		
	}
	*/
}
