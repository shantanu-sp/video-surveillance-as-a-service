package com.asu.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.asu.model.AwsEnvironment;

@Component
public class TestService {

	@Autowired
	AwsEnvironment env;
	
	public AwsEnvironment getEnv() {
		return env;
	}		
}
