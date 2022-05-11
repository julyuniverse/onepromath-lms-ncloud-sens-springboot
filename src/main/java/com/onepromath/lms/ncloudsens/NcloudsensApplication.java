package com.onepromath.lms.ncloudsens;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class NcloudsensApplication {

	public static void main(String[] args) {
		SpringApplication.run(NcloudsensApplication.class, args);
	}

}
