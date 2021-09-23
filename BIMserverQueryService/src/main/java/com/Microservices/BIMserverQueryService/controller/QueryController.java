package com.Microservices.BIMserverQueryService.controller;

import com.Microservices.BIMserverQueryService.connection.BIMserverConnection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RefreshScope
@RestController
// @Tag(name = "Delete", description = "Delete data from a GraphDB database")
public class QueryController {

  @Autowired
  BIMserverConnection bimserver;

  Logger log = LoggerFactory.getLogger(QueryController.class);

  @GetMapping("/querybimserver/create")
  public String createProject() {
    String results = "";

    results = bimserver.getConnection();

    return results;
 }
}