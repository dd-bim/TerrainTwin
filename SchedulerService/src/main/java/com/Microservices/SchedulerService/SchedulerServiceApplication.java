package com.Microservices.SchedulerService;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.servers.Server;

@EnableDiscoveryClient
@EnableScheduling
@SpringBootApplication
@Configuration
@EnableAutoConfiguration
@OpenAPIDefinition(info = @Info(title = "Scheduler API", description = "Documentation for automation of HTTP requests", version = "v1.1", license = @License(name = "Apache 2.0", url = "http://www.apache.org/licenses/LICENSE-2.0.html")))
public class SchedulerServiceApplication implements WebMvcConfigurer{

	public static void main(String[] args) throws IOException {
		SpringApplication.run(SchedulerServiceApplication.class, args);
	}

    @Bean
    TaskScheduler threadPoolTaskScheduler() {
        return new ThreadPoolTaskScheduler();
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
