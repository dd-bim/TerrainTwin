package com.Microservices.PostgresImportService.controller;

import java.util.UUID;

import com.Microservices.PostgresImportService.repositories.TINRepository;
import com.Microservices.PostgresImportService.service.CheckFiles;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
  CheckFiles minio;

  @Autowired
  TINRepository tinRepository;

  Logger log = LoggerFactory.getLogger(ImportRestController.class);

  // get folder and use them
  @GetMapping("/postgresimport/bucket/{bucket}/graphDbRepo/{graphDbRepo}")
  public String send(@PathVariable String bucket, @PathVariable String graphDbRepo) throws Exception {

    log.info("Start import of geometries into postgres database");
    String results = minio.getFiles(bucket, graphDbRepo);

    return results;
  }

    // get folder and use them
    @GetMapping("/postgres/dtm_tin/id/{id}")
    public String getDGM(@PathVariable UUID id) throws Exception {
  
    String geom = tinRepository.getTINGeometry(id);
  

      return geom;
    }
}