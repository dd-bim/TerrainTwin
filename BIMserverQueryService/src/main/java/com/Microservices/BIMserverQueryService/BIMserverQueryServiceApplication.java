package com.Microservices.BIMserverQueryService;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.servers.Server;

@EnableDiscoveryClient
@SpringBootApplication
@OpenAPIDefinition(info = @Info(title = "BIMserver Query API", description = "Documentation for querying projects on BIMserver", version = "v1.1", license = @License(name = "Apache 2.0", url = "http://www.apache.org/licenses/LICENSE-2.0.html")))
public class BIMserverQueryServiceApplication implements WebMvcConfigurer{

	public static void main(String[] args) throws IOException {
		SpringApplication.run(BIMserverQueryServiceApplication.class, args);
	}

	@Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS").allowedOrigins("*");
    }

    @Bean 
    public OpenAPI serversOpenAPI(@Value("${domain.url}") String url, @Value("${server.port}") String port) {
        return new OpenAPI().addServersItem(new Server().url(url)).addServersItem(new Server().url("http://localhost:" + port));
    }

}
