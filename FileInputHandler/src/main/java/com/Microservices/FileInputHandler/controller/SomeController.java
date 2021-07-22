package com.Microservices.FileInputHandler.controller;

import com.Microservices.FileInputHandler.connection.GraphDBConnection;
import com.Microservices.FileInputHandler.service.GetData;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@RefreshScope
@RestController
@RequestMapping("/inputhandler")
@Tag(name = "Other operations", description = "Do some more stuff")
public class SomeController {

  @Autowired
  GetData data;

  @Autowired
  GraphDBConnection dbconnection;

  Logger log = LoggerFactory.getLogger(SomeController.class);

  // get all namespaces
  @GetMapping(path = "/namespaces/repository/{repo}")
  @Operation(summary = "Get all namespaces used in the GraphDB repository")
  @ApiResponse(responseCode = "200", description = "Successful operation")
  public String getNamespaces(@PathVariable String repo) throws Exception {

    String result = data.getNamespace(repo);
    return result;
  }

  // create repository
  @GetMapping(path = "/createRepo/{repo}")
  @Operation(summary = "Create a new GraphDB repository")
  @ApiResponse(responseCode = "200", description = "Successful operation")
  public String createRepository(@PathVariable String repo) {
    String result = "";
    try {
      dbconnection.createRepo(repo);
      result = "Repo " + repo + " created.";
    } catch (Exception e) {
      result = "Problem: " + e.getMessage();
    }
    return result;
  }

}