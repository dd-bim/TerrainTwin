package com.graphdb.connection.controller;

import com.graphdb.connection.connectors.MinIOConnection;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.ExternalDocumentation;

@RestController
@RequestMapping("/terraintwin")
@CrossOrigin(origins = "http://localhost:8084")

@Tag(name = "GraphDBImporter", description = "Import semantic data from MinIo Object Storage into a GraphDB database", externalDocs = @ExternalDocumentation(url = "/terraintwin/graphdbimport/home", description = "Interface"))
public class ImportRestController {

  // get folder and use them
  @GetMapping("/graphdbimport/import")
  public String send(@RequestParam(name = "MinIO-Bucket") String folder,
      @RequestParam(name = "GraphDB-Repository") String repo, Model model) throws Exception {
    String results = "";

    MinIOConnection minio = new MinIOConnection();
    results += minio.getFiles(folder, repo);

    model.addAttribute("erg", results);

    return results;
  }
}