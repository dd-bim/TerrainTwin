package com.Microservices.BIMserverQueryService.controller;

import com.Microservices.BIMserverQueryService.connection.BIMserverConnection;
import com.Microservices.BIMserverQueryService.service.Functions;
import com.Microservices.BIMserverQueryService.service.Queries;

import org.bimserver.client.BimServerClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.tags.Tag;

@RefreshScope
@RestController
@Tag(name = "Model Queries", description = "Query information from a model")
public class QueryController {

  @Autowired
  BIMserverConnection bimserver;

  @Autowired
  Functions func;

  @Autowired
  Queries queries;

  Logger log = LoggerFactory.getLogger(QueryController.class);


  @GetMapping("/querybimserver/getAllWalls")
  public ResponseEntity<Resource> getWalls(@RequestParam String projectName) {

    // Queries queries = new Queries();
    BimServerClient client = bimserver.getConnection();
    // projectName = "Schependomlaan1";

    long[] pInfos = func.getProjectRoidAndSerializerOid(client, projectName);
    // return func.getFileByQuery(client, pInfos[0], pInfos[1], queries.getWalls());
    return func.getFileByQuery(client, pInfos[0], pInfos[1], queries.getElements("IfcWindow", false));
  }

}