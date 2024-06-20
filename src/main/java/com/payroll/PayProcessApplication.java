package com.payroll;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling

public class PayProcessApplication {

	public static void main(String[] args) {
		SpringApplication.run(PayProcessApplication.class, args);
	}

}
