package com.asu.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amazonaws.services.sqs.model.Message;

@Service
public class ListenerServiceImpl implements ListenerService {
	
	S3ServiceImpl awsS3;
	SqsServiceImpl inputSqsClient;
	SqsServiceImpl outputSqsClient;
	Ec2ServiceImpl awsEc2;
	String inputQueue;
	String opQueue;
	
	@Autowired
	public ListenerServiceImpl(S3ServiceImpl awsS3, SqsServiceImpl inpSqsQueueService, 
			Ec2ServiceImpl awsEc2,SqsServiceImpl opSqsQueueService, String sqsQueueName, String opSqsQueueName) {
//		super();
		this.awsS3 = awsS3;
		this.inputSqsClient = inpSqsQueueService;
		this.awsEc2 = awsEc2;
		this.outputSqsClient = opSqsQueueService;
		this.inputQueue = sqsQueueName;
		this.opQueue = opSqsQueueName;
		System.out.println("Lister Service Ready inp queue : "+ inputQueue 
		+ " op queue name : " + opQueue);
		
	}

	@Override
	public void lifecycle() {
		// TODO Auto-generated method stub
		try {
		while(true) {
			Message message = inputSqsClient.receiveMessage(this.inputQueue); //inputQ

			if(message == null) {
				System.out.println("No Messages left to process..1");
				Thread.sleep(20000);
				message = inputSqsClient.receiveMessage(this.inputQueue);
				if(message == null) {
					System.out.println("No Messages left to process..2");
					Thread.sleep(20000);
					message = inputSqsClient.receiveMessage(this.inputQueue);					
					if(message == null) {
						System.out.println("No Messages left to process..3");
						break;
					}
				}				
//				else
//				break;
			}
			
			inputSqsClient.deleteMessage(message,this.inputQueue);
			String fname = message.getBody();
			
			System.out.println("File to be deleted from sqs " + fname);
			//send filename to deep learning module 
			File videoInputFile = awsS3.getFileFromS3(fname);
			//File file=this.deeplearning(input);
			
			File deeplearningOpFile = deeplearning(videoInputFile);
			awsS3.uploadFileToS3OutputBucket(deeplearningOpFile);
			//awsS3.uploadFileToS3(deeplearningOpFile);
			
			//Delete files to free up memory.
			videoInputFile.delete();
			deeplearningOpFile.delete();
			//inputSqsClient.sendMessage(file.getName()); //outputQ
			outputSqsClient.sendMessage(message.getBody(),this.opQueue);
		}
		}
		catch(Exception e) {
			System.out.println(("Error in running App Instance! "+e.getMessage()));
		}
		awsEc2.endInstace();
	}

	private File deeplearning(File file) {
		// TODO Auto-generated method stub
		Path currentRelativePath = Paths.get("");
		String s = currentRelativePath.toAbsolutePath().toString();
		System.out.println("Current relative path is: " + s);
		//String outputFileName = file.getName()+"output.txt";
		String outputFileName = file.getName();
		if (outputFileName.contains(".")) {
		System.out.println(outputFileName);
		outputFileName = outputFileName.substring(0, outputFileName.lastIndexOf("."));
		System.out.println(outputFileName);
		}
		//utputFileName = outputFileName.split(".")[0];
		File opFile = new File(outputFileName);
		System.out.println("Op file path" + opFile.getAbsolutePath());
		try {
			opFile.createNewFile();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
//		Process process;
		try {
//			process = new ProcessBuilder("/bin/bash", "-c", "./darknet detector demo cfg/coco.data cfg/yolov3-tiny.cfg yolov3-tiny.weights",file.getAbsolutePath()).
//					redirectOutput(opFile).start();
			System.out.println("Executing deeplearning command.");
            ProcessBuilder pb = new ProcessBuilder("/bin/bash", "-c", "cd /home/ubuntu/darknet;./darknet detector demo cfg/coco.data cfg/yolov3-tiny.cfg yolov3-tiny.weights " + file.getAbsolutePath());
            Process process = pb.redirectOutput(opFile).start();
            int errCode = process.waitFor();
            System.out.println("\nExited with error code : " + errCode);
            

			//			process = new ProcessBuilder("cat",file.getAbsolutePath()).redirectOutput(opFile).start();
			process.waitFor();
			process.destroy();
		}catch(Exception e) {
			System.out.println(e);
		}
		
		FileReader fr = null;
		try {
			fr = new FileReader(opFile);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		BufferedReader br=new BufferedReader(fr);
		String s2="";
		String tmp = null;
		Boolean flag=false;
		try {
			while((tmp=br.readLine())!=null){
				String[] ts=tmp.split(":");
				
				if (ts[0].equals("FPS")) {
					flag=false;
				}
				
				if(flag==true) {
					if(ts.length>1) {
						if(s2.length()==0)
							s2+=ts[0];
						else
							if(!s2.contains(ts[0]))
								s2+=", "+ts[0];
					}
				}
				
				if(ts[0].equals("Objects")) {
					flag=true;
				}
			}

			br.close();
			fr.close();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		if(s2.length()==0)
			s2="No object detected";
		else
			s2 = file.getName() + ", " + s2;
		System.out.println(s2);
		FileWriter fw=null;
		BufferedWriter bw=null;
		try {
			fw=new FileWriter(opFile,false);
			bw=new BufferedWriter(fw);
			bw.write(s2);
			bw.close();
			fw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return opFile;
	}
	
	
}
