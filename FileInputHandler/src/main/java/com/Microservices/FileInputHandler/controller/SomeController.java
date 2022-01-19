package com.Microservices.FileInputHandler.controller;

import java.util.ArrayList;
import java.util.List;

import com.Microservices.FileInputHandler.connection.GraphDBConnection;
import com.Microservices.FileInputHandler.connection.GraphDBRestConnection;
import com.Microservices.FileInputHandler.domain.model.Queries;
import com.Microservices.FileInputHandler.service.QueryExecution;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@RefreshScope
@RestController
@RequestMapping("/inputhandler")
@Tag(name = "Other operations", description = "Do some more stuff")
public class SomeController {

  @Autowired
  Queries query;

  @Autowired
  QueryExecution exec;

  @Autowired
  GraphDBRestConnection restConn;

  @Autowired
  GraphDBConnection dbconnection;

  Logger log = LoggerFactory.getLogger(SomeController.class);

  // get all namespaces
  @GetMapping(path = "/namespaces/repository/{repo}")
  @Operation(summary = "Get all namespaces used in the GraphDB repository")
  @ApiResponse(responseCode = "200", description = "Successful operation")
  public String getNamespaces(@Parameter(description = "The name of the GraphDB repository.") @PathVariable String repo)
      throws Exception {

    String result = exec.getNamespace(repo);
    return result;
  }

  // create repository
  @GetMapping(path = "/createRepo/{repo}")
  @Operation(summary = "Create a new GraphDB repository")
  @ApiResponse(responseCode = "200", description = "Successful operation")
  public String createRepository(
      @Parameter(description = "The name of the GraphDB repository.") @PathVariable String repo) {
    String result = "";
    try {
      dbconnection.createRepo(repo);
      result = "Repo " + repo + " created.";
    } catch (Exception e) {
      result = "Problem: " + e.getMessage();
    }
    return result;
  }

  @GetMapping(path = "/repositories")
  // @Operation(summary = "Create a new GraphDB repository")
  @ApiResponse(responseCode = "200", description = "Successful operation")
  public String getRepos() {
    ArrayList<String> result = new ArrayList<String>();

    result = restConn.getRepositories();
    String json = new Gson().toJson(result);
    return json;
  }

  @GetMapping(path = "/geometry/dimension/repo/{repo}")
  @Operation(summary = "Get the dimension of a geometry object")
  @ApiResponse(responseCode = "200", description = "Successful operation")
  public String getGeoDim(@Parameter(description = "The name of the GraphDB repository.") @PathVariable String repo,
      @Parameter(description = "The geometry object IRI.") @RequestParam String geometry) {
    String result;

    result = exec.executeQuery(repo, query.getDimension(geometry));
    return result;
  }

  @GetMapping(path = "/geometry/infos1/repo/{repo}")
  @Operation(summary = "Get the dimension of a geometry object")
  @ApiResponse(responseCode = "200", description = "Successful operation")
  public String getInfos1(@Parameter(description = "The name of the GraphDB repository.") @PathVariable String repo,
      @Parameter(description = "The geometry object IRI.") @RequestParam String geometry) {

    ArrayList<List<String>> list = exec.executeQuery1(repo, query.getGeomInfos(geometry));

    JsonObject json = new JsonObject();
    json.addProperty("id", geometry);

    for (List<String> triple : list) {
      String key = getObject(triple.get(0));

      switch (key) {

        case "type":
          break;

        case "url":
          json.addProperty("postgresUrl", triple.get(1));
          break;

        default:
          String value = getObject(triple.get(1));
          json.addProperty(key, value);
          break;

      }
    }

    return json.toString();
  }

  @GetMapping(path = "/geometry/infos2/repo/{repo}")
  @Operation(summary = "Get the dimension of a geometry object")
  @ApiResponse(responseCode = "200", description = "Successful operation")
  public String getInfos2(@Parameter(description = "The name of the GraphDB repository.") @PathVariable String repo,
      @Parameter(description = "The geometry object IRI.") @RequestParam String geometry) {
    ArrayList<List<String>> list = exec.executeQuery1(repo, query.getTINUpdates(geometry));

    JsonObject json = new JsonObject();
    json.addProperty("id", geometry);
    JsonArray originalOf = new JsonArray();
    JsonArray inputFor = new JsonArray();
    JsonArray outputFrom = new JsonArray();
    ArrayList<String> inputArr = new ArrayList<String>();
    ArrayList<String> outputArr = new ArrayList<String>();
    ArrayList<String> originalArr = new ArrayList<String>();

    for (List<String> triple : list) {

      String input = getObject(triple.get(0));
      String output = getObject(triple.get(1));
      String original = getObject(triple.get(2));

      if (input.equals(geometry) && !inputArr.contains(output)) {
        inputFor.add(output);
        inputArr.add(output);
      } else if (output.equals(geometry) && !outputArr.contains(input)) {
        outputFrom.add(input);
        outputArr.add(input);
      }

      if (original.equals(geometry) && !originalArr.contains(output)) {
        originalOf.add(output);
        originalArr.add(output);
      }

    }

    json.add("newerVersion", inputFor);
    json.add("olderVersion", outputFrom);
    json.add("originalOf", originalOf);
    return json.toString();
  }

  @GetMapping(path = "/geometry/breaklines/repo/{repo}")
  @Operation(summary = "Get the dimension of a geometry object")
  @ApiResponse(responseCode = "200", description = "Successful operation")
  public String getBreaklines(@Parameter(description = "The name of the GraphDB repository.") @PathVariable String repo,
      @Parameter(description = "The geometry object IRI.") @RequestParam String geometry) {
    ArrayList<List<String>> list = exec.executeQuery1(repo, query.getBreaklinesFromGeom(geometry));

    JsonArray breaklines = new JsonArray();
    for (int i = 0; i < list.size(); i++) {
      breaklines.add(getObject(list.get(i).get(0)));
    }

    return breaklines.toString();
  }

  public String getObject(String iri) {
    String[] path = iri.replaceAll("\\#", "\\/").split("\\/");
    return path[path.length - 1];
  }

}