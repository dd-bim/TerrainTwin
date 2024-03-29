package com.Microservices.GeometryHandler.controller;

import com.Microservices.GeometryHandler.service.CheckFiles;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@RefreshScope
@RestController
@Tag(name = "Import")
@RequestMapping("/geometry/import")
public class ImportController {

  @Autowired
  CheckFiles minio;

  Logger log = LoggerFactory.getLogger(ImportController.class);

  // get folder and use them
  @GetMapping("/bucket/{bucket}/graphDbRepo/{graphDbRepo}")
  @Operation(summary = "Import geometries into Postgres from a bucket")
  @ApiResponse(responseCode = "200", description = "Successful operation")
  public String importFromBucket(@Parameter(description = "The name of the source MinIO bucket.") @PathVariable String bucket,
      @Parameter(description = "The name of the target repository for the import of extra information into GraphDB.") @PathVariable String graphDbRepo)
      throws Exception {

    log.info("Start import of geometries into postgres database");
    String results = minio.getGeoFromBucket(bucket, graphDbRepo);

    return results;
  }

  // get specific file from folder and use them
  @GetMapping("/bucket/{bucket}/graphDbRepo/{graphDbRepo}/file/{filename}")
  @Operation(summary = "Import geometries into Postgres from a specific file")
  @ApiResponse(responseCode = "200", description = "Successful operation")
  public String importFromFile(
      @Parameter(description = "The name of the source MinIO bucket.") @PathVariable String bucket,
      @Parameter(description = "The name of the target repository for the import of extra information into GraphDB.") @PathVariable String graphDbRepo,
      @Parameter(description = "Name of file, whitch should be imported.") @PathVariable String filename)
      throws Exception {

    log.info("Start import of geometries into postgres database");
    String results = minio.getGeoFromFile(bucket, graphDbRepo, filename);

    return results;
  }
}