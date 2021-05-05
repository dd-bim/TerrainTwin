package com.Microservices.PostgresImportService.connection;

import io.minio.MinioClient;

public class MinIOConnection {

    // Connect to MinIO
    public MinioClient connection(String url, String port, String access_key, String secret_key) {
        MinioClient client = MinioClient.builder().endpoint(url + ":" + port).credentials(access_key, secret_key)
                .build();
        return client;
    }
}
