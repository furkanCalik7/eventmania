package com.database.eventmania;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class EventmaniaApplication {

	public static void main(String[] args) {
		SpringApplication.run(EventmaniaApplication.class, args);
	}

}
