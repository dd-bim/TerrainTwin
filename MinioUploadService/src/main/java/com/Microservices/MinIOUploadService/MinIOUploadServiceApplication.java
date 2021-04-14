package com.Microservices.MinIOUploadService;

import java.io.IOException;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;

@EnableDiscoveryClient
@SpringBootApplication
@OpenAPIDefinition(info = @Info(title = "MinIO Upload API", description = "Documentation", version = "v1.0", license = @License(name = "Apache 2.0", url = "http://www.apache.org/licenses/")), servers = @Server(url = "http://localhost:7204"))
// @SecurityScheme(
//     name = "basicAuth",
// 	type = SecuritySchemeType.HTTP,
//     scheme = "basic"
// )
public class MinIOUploadServiceApplication {

	public static void main(String[] args) throws IOException {
		SpringApplication.run(MinIOUploadServiceApplication.class, args);
	}

}
