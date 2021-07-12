package com.Microservices.GraphDBImportService.controller;

import com.Microservices.GraphDBImportService.domain.model.PostgresInfos;
import com.Microservices.GraphDBImportService.domain.model.Queries;
import com.Microservices.GraphDBImportService.service.DeleteTriples;
import com.Microservices.GraphDBImportService.service.ImportFiles;
import com.Microservices.GraphDBImportService.service.ImportPostgresGeometryInfos;
import com.Microservices.GraphDBImportService.service.ImportTopology;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.DeleteMapping;
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
  ImportFiles minio;

  @Autowired
  ImportPostgresGeometryInfos postgres;

  @Autowired
  ImportTopology topo;

  @Autowired
  DeleteTriples triples;

  Logger log = LoggerFactory.getLogger(ImportRestController.class);

  // get bucket and use them
  @GetMapping("/graphdbimport/import/miniobucket/{bucket}/graphdbrepo/{repo}")
  public String send(@PathVariable String bucket, @PathVariable String repo) throws Exception {

    String results = minio.getFiles(bucket, repo);

    return results;
  }

  // import infos from postgres database geometry
  @PostMapping(path = "/graphdbimport/postgresinfos")
  public String importPostgres(@RequestBody PostgresInfos infos) throws Exception {

    String results = postgres.importPostgresInfos(infos);
    return results;
  }

  // import topological relations from postgres geometry
  @PostMapping(path = "/graphdbimport/topology/graphdbrepo/{repo}")
  public String importTopology(@PathVariable String repo, @RequestBody String topology) throws Exception {

    String result = topo.importTopo(topology, repo);
    return result;
  }

  // delete resource triples by geometry uuid
  @DeleteMapping(path = "/graphdbimport/delete/resource/repository/{repo}/id/{id}")
  public String delete(@PathVariable String repo, @PathVariable String id) throws Exception {
    String result = triples.delete(repo, id);
    return result;
  }

  // delete all topology triples
  @DeleteMapping(path = "/graphdbimport/delete/topology/repository/{repo}")
  public String deleteTopo(@PathVariable String repo) throws Exception {

    String result = triples.deleteTopology(repo);
    return result;
  }

  // excecute a delete query
  @DeleteMapping(path = "/graphdbimport/delete/query/repository/{repo}")
  public String deleteQuery(@PathVariable String repo, @RequestBody Queries query) throws Exception {
    String result = triples.deleteQuery(repo, query);
    return result;
  }

  // get all namespaces
  @GetMapping(path = "/graphdbimport/namespaces/repository/{repo}")
  public String getNamespaces(@PathVariable String repo) throws Exception {

    String result = triples.getNamespace(repo);
    return result;
  }

}