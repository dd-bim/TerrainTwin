package com.graphdb.connection;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class ConnectionApplication {

    public static void main(String[] args) throws Exception {
        SpringApplication.run(ConnectionApplication.class, args);
    }

}
