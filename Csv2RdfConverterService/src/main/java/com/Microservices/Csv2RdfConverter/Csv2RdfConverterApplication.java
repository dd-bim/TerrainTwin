package com.Microservices.Csv2RdfConverter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class Csv2RdfConverterApplication {

	public static void main(String[] args) {
		SpringApplication.run(Csv2RdfConverterApplication.class, args);
	}

}
