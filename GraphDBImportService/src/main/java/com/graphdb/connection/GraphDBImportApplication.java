package com.graphdb.connection;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;

@EnableDiscoveryClient
@SpringBootApplication
public class GraphDBImportApplication {

    public static void main(String[] args) throws Exception {
        SpringApplication.run(GraphDBImportApplication.class, args);
    }

    @Bean
    public OpenAPI infosOpenAPI() {
        return new OpenAPI().info(new Info().title("GraphDBImport API").description("Documentation").version("v1.0")
                .license(new License().name("Apache 2.0").url("http://www.apache.org/licenses/")));

    }

}
