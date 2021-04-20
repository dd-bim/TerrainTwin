package com.Microservices.Csv2RdfConverter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.servers.Server;

@EnableDiscoveryClient
@SpringBootApplication
@OpenAPIDefinition(info = @Info(title = "Csv2RdfConverter API", description = "Documentation", version = "v1.0", license = @License(name = "Apache 2.0", url = "http://www.apache.org/licenses/")), servers = {@Server(url ="https://terrain.dd-bim.org"), @Server(url ="http://localhost:7202")})

public class Csv2RdfConverterApplication implements WebMvcConfigurer{

	public static void main(String[] args) {
		SpringApplication.run(Csv2RdfConverterApplication.class, args);
	}

	@Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS").allowedOrigins("*");
    }
}
