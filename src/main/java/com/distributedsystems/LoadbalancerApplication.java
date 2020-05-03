package com.distributedsystems;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.distributedsystems")
public class LoadbalancerApplication {

	public static void main(String[] args) {

		SpringApplication.run(LoadbalancerApplication.class, args);
	}

}
