package com.Microservices.Csv2RdfConverter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;

@EnableDiscoveryClient
@SpringBootApplication
public class Csv2RdfConverterApplication {

	public static void main(String[] args) {
		SpringApplication.run(Csv2RdfConverterApplication.class, args);
	}

	@Bean
	public OpenAPI infosOpenAPI() {
		return new OpenAPI().info(new Info().title("Csv2RdfConverter API").description("Documentation").version("v1.0")
				.license(new License().name("Apache 2.0").url("http://www.apache.org/licenses/")));

	}
}
