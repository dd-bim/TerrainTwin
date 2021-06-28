package com.Microservices.GraphDBImportService.controller;

import com.Microservices.GraphDBImportService.domain.model.PostgresInfos;
import com.Microservices.GraphDBImportService.service.ImportService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RefreshScope
@RestController
@Tag(name = "GraphDBImporter", description = "Import semantic data into a GraphDB database", externalDocs = @ExternalDocumentation(url = "/graphdbimport/home", description = "Interface"))
public class ImportRestController {

  @Autowired
  ImportService minio;

  Logger log = LoggerFactory.getLogger(ImportRestController.class);

  // get bucket and use them
  @GetMapping("/graphdbimport/import/miniobucket/{bucket}/graphdbrepo/{repo}")
  public String send(@PathVariable String bucket, @PathVariable String repo) throws Exception {

    String results = minio.getFiles(bucket, repo);

    return results;
  }

  // import infos from postgres database geometry
  @PostMapping(path = "/graphdbimport/postgresinfos")
  public String importPostgres(@RequestBody PostgresInfos infos ) throws Exception {

    String results = minio.importPostgresInfos(infos);

    return results;
  }

    // import infos from postgres database geometry
    @PostMapping(path = "/graphdbimport/topology")
    public String importTopology(@RequestBody String topo ) throws Exception {
  
      log.info(topo);
      return "test";
    }
}