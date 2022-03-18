package com.forum.topicos;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class TopicosApplication {

	public static void main(String[] args) {
		SpringApplication.run(TopicosApplication.class, args);
	}

}
