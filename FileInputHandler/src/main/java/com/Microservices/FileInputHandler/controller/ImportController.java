package com.Microservices.FileInputHandler.controller;

import com.Microservices.FileInputHandler.connection.GraphDBConnection;
import com.Microservices.FileInputHandler.domain.model.PostgresInfos;
import com.Microservices.FileInputHandler.domain.model.Triple;
import com.Microservices.FileInputHandler.service.DeleteTriples;
import com.Microservices.FileInputHandler.service.ImportFiles;
import com.Microservices.FileInputHandler.service.ImportPostgresGeometryInfos;
import com.Microservices.FileInputHandler.service.ImportTopology;
import com.Microservices.FileInputHandler.service.ImportTriple;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@RefreshScope
@RestController
@RequestMapping("/inputhandler/import")
@Tag(name = "Import", description = "Import semantic data into a GraphDB database")
public class ImportController {

  @Autowired
  ImportFiles minio;

  @Autowired
  ImportPostgresGeometryInfos postgres;

  @Autowired
  ImportTopology topo;

  @Autowired
  DeleteTriples triples;

  @Autowired 
  GraphDBConnection dbconnection;

  @Autowired
  ImportTriple impTriple;

  Logger log = LoggerFactory.getLogger(ImportController.class);

  // import files from bucket
  @GetMapping("/miniobucket/{bucket}/repository/{repo}")
  @Operation(summary = "Read files from MinIO bucket and import data into a GraphDB repository")
  @ApiResponse(responseCode = "200", description = "Successful operation")
  @ApiResponse(responseCode = "500", description = "File is not valid for a conversion to RDF syntax")
  public String send(@Parameter(description = "The name of the source MinIO bucket.")  @PathVariable String bucket, @Parameter(description = "The name of the GraphDB repository.") @PathVariable String repo) throws Exception {

    String results = minio.getFiles(bucket, repo);

    return results;
  }

  // import infos from postgres database geometry
  @PostMapping(path = "/postgresinfos")
  @Operation(summary = "Import infos of Postgres geometries")
  @ApiResponse(responseCode = "200", description = "Successful operation")
  public String importPostgres(@Parameter(description = "Some metadata of postgres geometry.") @RequestBody PostgresInfos infos) throws Exception {

    String results = postgres.importPostgresInfos(infos);
    return results;
  }

  // import topological relations from postgres geometry
  @PostMapping(path = "/topology/repository/{repo}")
  @Operation(summary = "Import topological relations of Postgres geometries")
  @ApiResponse(responseCode = "200", description = "Successful operation")
  public String importTopology(@Parameter(description = "The name of the GraphDB repository.") @PathVariable String repo, @Parameter(description = "The topology triples to be imported.") @RequestBody String topology) throws Exception {

    String result = topo.importTopo(topology, repo);
    return result;
  }

    // import infos from postgres database geometry
    @PostMapping(path = "/triple/repository/{repo}")
    @Operation(summary = "Import a triple")
    @ApiResponse(responseCode = "200", description = "Successful operation")
    public String importTriple(@Parameter(description = "The name of the GraphDB repository.") @PathVariable String repo, @Parameter(description = "Subject, predicate and object of a triple") @RequestBody Triple triple) throws Exception {
  
      String results = impTriple.importTriple(repo, triple);
      return results;
    }
}