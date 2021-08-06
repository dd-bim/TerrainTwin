package com.Microservices.FileInputHandler.controller;

import com.Microservices.FileInputHandler.connection.GraphDBConnection;
import com.Microservices.FileInputHandler.domain.model.UpdateQuery;
import com.Microservices.FileInputHandler.service.UpdateTriples;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
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
@RequestMapping("/inputhandler/update")
@Tag(name = "Update", description = "Update data on a GraphDB database")
public class UpdateController {

  @Autowired
  UpdateTriples uQuery;

  @Autowired 
  GraphDBConnection dbconnection;

  Logger log = LoggerFactory.getLogger(UpdateController.class);


  // excecute a update query
  @PostMapping(path = "/repository/{repo}")
  @Operation(summary = "Update one or more triples from a GraphDB repository by creating a delete and insert query")
  @ApiResponse(responseCode = "200", description = "Successful operation")
  public String updateQuery(@Parameter(description = "The name of the GraphDB repository.") @PathVariable String repo, @RequestBody UpdateQuery query) throws Exception {
    String result = uQuery.update(repo, query);
    return result;
  }
}