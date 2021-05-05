package com.Microservices.PostgresImportService.controller;

import com.Microservices.PostgresImportService.connection.MinIOConnection;
import com.Microservices.PostgresImportService.repositories.BreaklinesRepository;
import com.Microservices.PostgresImportService.repositories.SurfaceRepository;
import com.Microservices.PostgresImportService.repositories.TINRepository;
import com.Microservices.PostgresImportService.service.CheckFiles;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RefreshScope
@RestController
@Tag(name = "Postgres Importer", description = "Import Files from MinIO Object Storage into postgres database", externalDocs = @ExternalDocumentation(url = "/postgresimport", description = "Interface"))
public class ImportRestController {

  @Autowired
  SurfaceRepository repository;
  @Autowired
  TINRepository tinRepository;
  @Autowired
  BreaklinesRepository blRepository;

  Logger log = LoggerFactory.getLogger(ImportRestController.class);

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

    MinIOConnection connect = new MinIOConnection();
    CheckFiles minio = new CheckFiles(connect.connection(url, port, access_key, secret_key));
    String results = minio.getFiles(bucket, tinRepository, blRepository, repository);

    return results;
  }
}