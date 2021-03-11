package com.Microservices.GatewayService;

import java.util.ArrayList;
import java.util.List;

import org.springdoc.core.GroupedOpenApi;
import org.springdoc.core.SwaggerUiConfigParameters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionLocator;
import org.springframework.context.annotation.Bean;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;

@EnableDiscoveryClient
@SpringBootApplication
public class GatewayServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(GatewayServiceApplication.class, args);
	}

	// @Bean
	// public OpenAPI infosOpenAPI() {
	// return new OpenAPI().info(new Info().title("TerrainTwin
	// API").description("Documentation").version("v1.0")
	// .license(new License().name("Apache
	// 2.0").url("http://www.apache.org/licenses/")));
	// }

	@Autowired
	RouteDefinitionLocator locator;

	@Bean
	public List<GroupedOpenApi> apis(SwaggerUiConfigParameters swaggerUiConfigParameters,
			RouteDefinitionLocator locator) {
		List<GroupedOpenApi> groups = new ArrayList<>();
		List<RouteDefinition> definitions = locator.getRouteDefinitions().collectList().block();
		for (RouteDefinition definition : definitions) {
			System.out.println("id: " + definition.getId() + "  " + definition.getUri().toString());
		}
		definitions.stream().filter(routeDefinition -> routeDefinition.getId().matches(".*-service"))
				.forEach(routeDefinition -> {
					String name = routeDefinition.getId().replaceAll("-service", "");
					swaggerUiConfigParameters.addGroup(name);
					GroupedOpenApi.builder().pathsToMatch("/" + name + "/**").group(name).build();
				});
		return groups;
	}
}
