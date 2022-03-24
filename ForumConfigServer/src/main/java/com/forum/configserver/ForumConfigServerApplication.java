package com.forum.configserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;

@SpringBootApplication
@EnableConfigServer
public class ForumConfigServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(ForumConfigServerApplication.class, args);
	}

}
