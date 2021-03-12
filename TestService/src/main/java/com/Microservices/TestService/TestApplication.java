package com.Microservices.TestService;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.servers.Server;

@EnableDiscoveryClient
@SpringBootApplication
@RestController
@OpenAPIDefinition(info = @Info(title = "Test API", description = "Documentation", version = "v1.0", license = @License(name = "Apache 2.0", url = "http://www.apache.org/licenses/")), servers = @Server(url = "http://localhost:7202"))
public class TestApplication {

	@Value("${user.role}")
	private String role;
	@Value("${test.name}")
	private String test;

	public static void main(String[] args) {
		SpringApplication.run(TestApplication.class, args);
	}

	@GetMapping(value = "/whoami/{username}", produces = MediaType.TEXT_PLAIN_VALUE)
	public String whoami(@PathVariable("username") String username) {
		return String.format("Hello! You're %s and you'll become a(n) %s...\n %s", username, role, test);
	}

}
