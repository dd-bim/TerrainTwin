package com.Microservices.GraphDBImportService;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.servers.Server;

@EnableDiscoveryClient
@SpringBootApplication
@OpenAPIDefinition(info = @Info(title = "GraphDBImport API", description = "Documentation", version = "v1.0", license = @License(name = "Apache 2.0", url = "http://www.apache.org/licenses/")), servers = @Server(url = "http://localhost:7201"))
public class GraphDBImportApplication {

    public static void main(String[] args) throws Exception {
        SpringApplication.run(GraphDBImportApplication.class, args);
    }

}
