package com.graphdb.connection.controller;

import com.graphdb.connection.service.MinIOConnection;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/terraintwin")
@CrossOrigin(origins = "http://localhost:8084")
@Tag(name = "GraphDBImporter", description = "Import semantic data from MinIo Object Storage into a GraphDB database", externalDocs = @ExternalDocumentation(url = "/terraintwin/graphdbimport/home", description = "Interface"))
public class ImportRestController {

  // get bucket and use them
  @GetMapping("/graphdbimport/import/miniobucket/{bucket}/graphdbrepo/{repo}")
  public String send(@PathVariable String bucket, @PathVariable String repo) throws Exception {
    MinIOConnection minio = new MinIOConnection();
    String results = minio.getFiles(bucket, repo);

    return results;
  }
}