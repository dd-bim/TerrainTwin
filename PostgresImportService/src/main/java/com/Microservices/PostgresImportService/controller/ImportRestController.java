package com.Microservices.PostgresImportService.controller;

import com.Microservices.PostgresImportService.repositories.BreaklinesRepository;
import com.Microservices.PostgresImportService.repositories.SurfaceRepository;
import com.Microservices.PostgresImportService.repositories.TINRepository;
import com.Microservices.PostgresImportService.service.MinIOConnection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

@RefreshScope
@RestController
@Slf4j
@RequestMapping("/terraintwin")
@CrossOrigin(origins = "http://localhost:8084")
@Tag(name = "Postgres Importer", description = "Import Files from MinIO Object Storage into postgres database", externalDocs = @ExternalDocumentation(url = "/terraintwin/postgresimport", description = "Interface"))
public class ImportRestController {

  @Autowired
  SurfaceRepository repository;
  @Autowired
  TINRepository tinRepository;
  @Autowired
  BreaklinesRepository blRepository;

  @Value("${minio.url}")
  private String url;
  @Value("${minio.port}")
  private String port;
  @Value("${minio.access_key}")
  private String access_key;
  @Value("${minio.secret_key}")
  private String secret_key;

  // get folder and use them
  @GetMapping("/postgresimport/bucket/{bucket}")
  public String send(@PathVariable String bucket) throws Exception {
    log.info("Start import of geometries into postgres database");

    MinIOConnection minio = new MinIOConnection(url,port,access_key, secret_key);
    String results = minio.getFiles(bucket, tinRepository, blRepository, repository);

    return results;
  }
}