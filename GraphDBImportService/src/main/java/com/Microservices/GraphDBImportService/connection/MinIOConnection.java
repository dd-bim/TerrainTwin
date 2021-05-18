package com.Microservices.GraphDBImportService.connection;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import io.minio.MinioClient;

@Configuration
public class MinIOConnection {

    @Value("${minio.url}")
    private String url;
    @Value("${minio.port}")
    private String port;
    @Value("${minio.access_key}")
    private String access_key;
    @Value("${minio.secret_key}")
    private String secret_key;

    // Connect to MinIO
    public MinioClient connection() {
        MinioClient client = MinioClient.builder().endpoint(url + ":" + port).credentials(access_key, secret_key)
                .build();
        return client;
    }
}
