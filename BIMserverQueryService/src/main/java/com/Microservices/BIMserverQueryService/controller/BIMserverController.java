package com.Microservices.BIMserverQueryService.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;

import com.Microservices.BIMserverQueryService.connection.BIMserverConnection;
import com.Microservices.BIMserverQueryService.domain.EnumIfcVersion;
import com.Microservices.BIMserverQueryService.service.Functions;

import org.bimserver.client.BimServerClient;
import org.bimserver.interfaces.objects.SDeserializerPluginConfiguration;
import org.bimserver.interfaces.objects.SLongCheckinActionState;
import org.bimserver.interfaces.objects.SProject;
import org.bimserver.interfaces.objects.SRevision;
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

import io.swagger.v3.oas.annotations.tags.Tag;

@RefreshScope
@RestController
@Tag(name = "General Queries", description = "General Queries on BIMserver")
public class BIMserverController {

  @Autowired
  BIMserverConnection bimserver;

  @Autowired
  Functions func;

  Logger log = LoggerFactory.getLogger(BIMserverController.class);

  @GetMapping("/querybimserver/create")
  public String createProject(@RequestParam String name, @RequestParam EnumIfcVersion version)
      throws ServerException, UserException, PublicInterfaceNotFoundException {
    String result = "";

    BimServerClient client = bimserver.getConnection();

    try {
      SProject newProject = client.getServiceInterface().addProject(name, version.toString());
      result = "Oid: " + newProject.getOid();
    } catch (Exception e) {
      result = e.getMessage();
    }

    return result;
  }

  @GetMapping(path = "/querybimserver/checkin")
  public String checkinFile(@RequestParam Long poid)
      throws ServerException, UserException, PublicInterfaceNotFoundException, IllegalStateException, IOException {
    String result = "";
    BimServerClient client = bimserver.getConnection();

    File file = new File("/app/files/Haus14d.ifc");

    // Look for a deserializer
    SDeserializerPluginConfiguration deserializer = client.getServiceInterface()
        .getSuggestedDeserializerForExtension("ifc", poid);

    DataSource source = new FileDataSource(file);
    DataHandler data = new DataHandler(source);
    try {
      SLongCheckinActionState state = client.getServiceInterface().checkinSync(poid, "", deserializer.getOid(),
          file.getTotalSpace(), file.getName(), data, false);
      result = "\n Title: " + state.getTitle() + "\n Oid: " + state.getOid() + "\n Roid: " + state.getRoid()
          + "\n Rid: " + state.getRid() + "\n Stage: " + state.getStage() + "\n TopicId: " + state.getTopicId()
          + "\n Progress: " + state.getProgress() + "\n Uuid: " + state.getUuid() + "\n State: " + state.getState();

    } catch (Exception e) {
      result = e.getMessage();
    }

    return result;
  }

  @GetMapping("/querybimserver/getAllProjects")
  public String getAllProjects() throws ServerException, UserException, PublicInterfaceNotFoundException {
    BimServerClient client = bimserver.getConnection();
    return func.getAllProjects(client);
  }

  @GetMapping("/querybimserver/createAndCheckin")
  public String createAndCheckin()
      throws ServerException, UserException, PublicInterfaceNotFoundException, IOException {
    String result = "";

    BimServerClient client = bimserver.getConnection();

    File file = new File("/app/files/Haus14d.ifc");
    String pName = file.getName().replace(".ifc", "");
    String version = null;

    BufferedReader reader = new BufferedReader(new FileReader(file));
    while (reader.readLine() != null && version == null) {
      String line = reader.readLine();
      if (line.contains("FILE_SCHEMA")) {
        if (line.toUpperCase().contains("IFC4")) {
          version = "IFC4";
        } else if (line.toUpperCase().contains("IFC2X3")) {
          version = "IFC2X3TC1";
        }
      }
    }
    reader.close();

    SProject newProject = client.getServiceInterface().addProject(pName, version);
    result = "Poid: " + newProject.getOid() + "\n";

    SDeserializerPluginConfiguration deserializer = client.getServiceInterface()
        .getSuggestedDeserializerForExtension("ifc", newProject.getOid());

    DataSource source = new FileDataSource(file);
    DataHandler data = new DataHandler(source);
    try {
      SLongCheckinActionState state = client.getServiceInterface().checkinSync(newProject.getOid(), "",
          deserializer.getOid(), file.getTotalSpace(), file.getName(), data, false);
      result += "\n Title: " + state.getTitle() + "\n Oid: " + state.getOid() + "\n Roid: " + state.getRoid()
          + "\n Rid: " + state.getRid() + "\n Stage: " + state.getStage() + "\n TopicId: " + state.getTopicId()
          + "\n Progress: " + state.getProgress() + "\n Uuid: " + state.getUuid() + "\n State: " + state.getState();

    } catch (Exception e) {
      result = e.getMessage();
    }

    return result;
  }

  @GetMapping("/querybimserver/getRevisionId")
  public String getRevisionId() {

    String result = "";
    BimServerClient client = bimserver.getConnection();
    List<SRevision> revs = null;
    try {
      revs = client.getServiceInterface().getAllRevisionsOfProject((long) 2490369);
    } catch (ServerException | UserException | PublicInterfaceNotFoundException e) {
      e.printStackTrace();
    }

    for(SRevision revision : revs) {
      result += revision.getOid() + "\n";
      revision.getUuid();
    }

    return result;
  }

}