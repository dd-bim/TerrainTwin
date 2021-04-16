package com.Microservices.GraphDBImportService.controller;

import com.Microservices.GraphDBImportService.service.ImportService;

import org.springframework.beans.factory.annotation.Value;
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

  @Value("${minio.url}")
  private String url;
  @Value("${minio.port}")
  private String port;
  @Value("${minio.access_key}")
  private String access_key;
  @Value("${minio.secret_key}")
  private String secret_key;

  @Value("${graphdb.url}")
  private String graphdb_url;
  @Value("${graphdb.username}")
  private String graphdb_username;
  @Value("${graphdb.password}")
  private String graphdb_password;

  // get bucket and use them
  @GetMapping("/graphdbimport/import/miniobucket/{bucket}/graphdbrepo/{repo}")
  public String send(@PathVariable String bucket, @PathVariable String repo) throws Exception {
    ImportService minio = new ImportService(url, port, access_key, secret_key, graphdb_url, graphdb_username, graphdb_password);
    String results = minio.getFiles(bucket, repo, url);

    return results;
  }
}