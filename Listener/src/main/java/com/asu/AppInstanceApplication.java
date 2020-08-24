package com.asu;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;

import com.asu.service.ListenerService;

@SpringBootApplication
public class AppInstanceApplication {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ConfigurableApplicationContext context=SpringApplication.run(AppInstanceApplication.class, args);
		ListenerService ls=context.getBean(ListenerService.class);
		ls.lifecycle();
		context.close();
	}

}
