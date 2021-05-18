package com.Microservices.GraphDBImportService.controller;

import com.Microservices.GraphDBImportService.service.ImportService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RefreshScope
@RestController
@Tag(name = "GraphDBImporter", description = "Import semantic data from MinIo Object Storage into a GraphDB database", externalDocs = @ExternalDocumentation(url = "/graphdbimport/home", description = "Interface"))
public class ImportRestController {

  @Autowired
  ImportService minio;

  // get bucket and use them
  @GetMapping("/graphdbimport/import/miniobucket/{bucket}/graphdbrepo/{repo}")
  public String send(@PathVariable String bucket, @PathVariable String repo) throws Exception {

    String results = minio.getFiles(bucket, repo);

    return results;
  }
}