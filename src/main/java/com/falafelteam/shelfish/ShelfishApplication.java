package com.falafelteam.shelfish;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ShelfishApplication {

	public static void main(String[] args) {
		SpringApplication.run(ShelfishApplication.class, args);
	}
}