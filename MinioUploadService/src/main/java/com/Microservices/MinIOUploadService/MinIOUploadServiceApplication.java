package com.Microservices.MinIOUploadService;

import java.io.IOException;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class MinIOUploadServiceApplication {

	public static void main(String[] args) throws IOException {
		SpringApplication.run(MinIOUploadServiceApplication.class, args);
		// ProcessBuilder pb = new ProcessBuilder("node", "minioUpload/server.js");
		// ProcessBuilder pb = new ProcessBuilder("cmd.exe", "/D",
		// "minioUpload/start.bat");
		// ProcessBuilder pb = new ProcessBuilder("node
		// ../../../../../../minioUpload/server.js");
		// pb.redirectOutput(ProcessBuilder.Redirect.INHERIT);
		// pb.redirectError(ProcessBuilder.Redirect.INHERIT);
		// Process p = pb.start();
	}

}
