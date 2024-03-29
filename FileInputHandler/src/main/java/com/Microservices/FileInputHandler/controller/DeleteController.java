package com.Microservices.FileInputHandler.controller;

import com.Microservices.FileInputHandler.connection.GraphDBConnection;
import com.Microservices.FileInputHandler.domain.model.DeleteQuery;
import com.Microservices.FileInputHandler.service.DeleteTriples;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@RefreshScope
@RestController
@RequestMapping("/inputhandler/delete")
@Tag(name = "Delete", description = "Delete data from a GraphDB database")
public class DeleteController {

  @Autowired
  DeleteTriples triples;

  @Autowired 
  GraphDBConnection dbconnection;

  Logger log = LoggerFactory.getLogger(DeleteController.class);

  // delete resource triples by geometry uuid
  @DeleteMapping(path = "/resource/repository/{repo}/id/{id}")
  @Operation(summary = "Delete all triples containing a resource identified by id from a GraphDB repository")
  @ApiResponse(responseCode = "200", description = "Successful operation")
  public String delete(@Parameter(description = "The name of the GraphDB repository.") @PathVariable String repo, @Parameter(description = "The id of the resource, which should be deleted.") @PathVariable String id) throws Exception {
    String result = triples.delete(repo, id);
    return result;
  }

  // delete all topology triples
  @DeleteMapping(path = "/topology/repository/{repo}")
  @Operation(summary = "Delete all topological relations of Postgres geometries from a GraphDB repository")
  @ApiResponse(responseCode = "200", description = "Successful operation")
  public String deleteTopo(@Parameter(description = "The name of the GraphDB repository.") @PathVariable String repo) throws Exception {

    String result = triples.deleteTopology(repo);
    return result;
  }

  // excecute a delete query
  @DeleteMapping(path = "/repository/{repo}")
  @Operation(summary = "Delete triples from a GraphDB repository by creating a delete query")
  @ApiResponse(responseCode = "200", description = "Successful operation")
  public String deleteQuery(@Parameter(description = "The name of the GraphDB repository.") @PathVariable String repo, @Parameter(description = "The parameters to create a delete query.") @RequestBody DeleteQuery query) throws Exception {
    String result = triples.deleteAsQuery(repo, query);
    return result;
  }
}