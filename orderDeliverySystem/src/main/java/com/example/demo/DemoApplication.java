package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.StateMachine;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class DemoApplication  {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}
//	@Autowired
//	private StateMachine<States, Events> stateMachine;
//
//	@Override
//	public void run(String... args) throws Exception {
////		stateMachine.sendEvent(Events.E1);
////		stateMachine.sendEvent(Events.E2);
//	}
}
