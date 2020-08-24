package com.asu.app;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;

import com.asu.config.AwsServiceClientConfig;
import com.asu.service.AutoScaling;


@SpringBootApplication
@ComponentScan("com.asu")
public class AutoScaleWebControllerApplication implements CommandLineRunner{

	
	public static void main(String[] args) {
		ConfigurableApplicationContext context = SpringApplication.run(AutoScaleWebControllerApplication.class, args);
		AutoScaling impl = context.getBean(AutoScaling.class);
		impl.LaunchInstances();
//		try {
//			Thread.sleep(10000);
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
//	    AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext(AwsServiceClientConfig.class);
//
//		
//		ctx.register(LoadBalancingConfig.class);
		
		//ctx.refresh();
//		System.out.println(ctx.toString());
//		//ctx.getBean(EC2ServiceImpl.class);
//		AutoScaling impl = ctx.getBean(AutoScaling.class);
//		System.out.println("impl got!");
//		impl.LaunchInstances();
	}

	@Override
	public void run(String... args) throws Exception {
		// TODO Auto-generated method stub
		
	}
	   
//	@Override
//	    public void run(String... args) throws Exception {
//
//	        String[] beans = appContext.getBeanDefinitionNames();
//	        Arrays.sort(beans);
//	        for (String bean : beans) {
//	            System.out.println(bean);
//	        }
//
//	    }
	
}
