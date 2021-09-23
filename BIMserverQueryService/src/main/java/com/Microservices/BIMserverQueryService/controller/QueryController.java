package com.Microservices.BIMserverQueryService.controller;

import com.Microservices.BIMserverQueryService.connection.BIMserverConnection;
import com.Microservices.BIMserverQueryService.domain.EnumIfcVersion;

import org.bimserver.client.BimServerClient;
import org.bimserver.interfaces.objects.SProject;
import org.bimserver.shared.exceptions.PublicInterfaceNotFoundException;
import org.bimserver.shared.exceptions.ServerException;
import org.bimserver.shared.exceptions.UserException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RefreshScope
@RestController
// @Tag(name = "Delete", description = "Delete data from a GraphDB database")
public class QueryController {

  @Autowired
  BIMserverConnection bimserver;

  Logger log = LoggerFactory.getLogger(QueryController.class);

  @GetMapping("/querybimserver/create")
  public String createProject(@RequestParam String name, @RequestParam EnumIfcVersion version) throws ServerException, UserException, PublicInterfaceNotFoundException {
    String result = "";
log.info("V: " + version);
log.info("V1: " + version.toString());
    BimServerClient client = bimserver.getConnection();

    SProject newProject = client.getServiceInterface().addProject(name, version.toString());
    System.out.println(newProject.getOid());
    result = "Oid: " + newProject.getOid();

    return result;
  }
}