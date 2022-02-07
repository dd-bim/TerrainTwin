package com.Microservices.FileInputHandler.controller;

import java.util.ArrayList;
import java.util.List;

import com.Microservices.FileInputHandler.connection.GraphDBRestConnection;
import com.Microservices.FileInputHandler.domain.model.Queries;
import com.Microservices.FileInputHandler.service.QueryExecution;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
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
@Tag(name = "Request", description = "Get some information from GraphDB")
public class RequestController {

  @Autowired
  Queries query;

  @Autowired
  QueryExecution exec;

  @Autowired
  GraphDBRestConnection restConn;

  Logger log = LoggerFactory.getLogger(RequestController.class);

  // get all namespaces
  @GetMapping(path = "/namespaces/repository/{repo}")
  @Operation(summary = "Get all namespaces used in the GraphDB repository")
  @ApiResponse(responseCode = "200", description = "Successful operation")
  public String getNamespaces(@Parameter(description = "The name of the GraphDB repository.") @PathVariable String repo)
      throws Exception {

    String result = exec.getNamespace(repo);
    return result;
  }

  // get a list of all repositories
  @GetMapping(path = "/repositories")
  @Operation(summary = "Get all existing repositories from GraphDB")
  @ApiResponse(responseCode = "200", description = "Successful operation")
  public String getRepos() {
    ArrayList<String> result = new ArrayList<String>();
    result = restConn.getRepositories();
    String json = new Gson().toJson(result);
    return json;
  }

  // get the dimension (point = 0, line = 1, polygon = 2, solid = 3, tin = 4)
  @GetMapping(path = "/geometry/dimension/repo/{repo}")
  @Operation(summary = "Get the dimension of a geometry object", description = "Possible results: <br>0 = Point, <br>1 = Line, <br>2 = Polygon, <br>3 = Solid, <br>4 = TIN")
  @ApiResponse(responseCode = "200", description = "Successful operation")
  public String getGeoDim(@Parameter(description = "The name of the GraphDB repository.") @PathVariable String repo,
      @Parameter(description = "The geometry object id.") @RequestParam String geometry) {
    String result;

    result = exec.executeQuery(repo, query.getDimension(geometry));
    return result;
  }

  // get most information about a geometry at once
  @GetMapping(path = "/geometry/tinInfos/repo/{repo}")
  @Operation(summary = "Get infos of a geometry object")
  @ApiResponse(responseCode = "200", description = "Successful operation")
  public String getInfos1(@Parameter(description = "The name of the GraphDB repository.") @PathVariable String repo,
      @Parameter(description = "The geometry object id.") @RequestParam String geometry) {

    // get general information about geometry
    ArrayList<List<String>> list = exec.executeQuery1(repo, query.getGeomInfos(geometry));

    JsonObject json = new JsonObject();

    // add the geometry id for information
    json.addProperty("id", geometry);

    for (List<String> triple : list) {
      String key = getObject(triple.get(0));

      switch (key) {

        // ignore class definitions from ontology, expressed by type
        case "type":
          break;

        // set the url to the geometry in Postgres
        case "url":
          json.addProperty("postgresUrl", triple.get(1));
          break;

        // add all other properties as key value pairs
        default:
          String value = getObject(triple.get(1));
          json.addProperty(key, value);
          break;
      }
    }

    // try to get source file name
    String source = exec.executeQuery(repo, query.getSourceFile(geometry));
    if (source.isEmpty()) {
      ArrayList<List<String>> uList = exec.executeQuery1(repo, query.getInfosForTINUpdate(geometry));
      source = uList.get(0).get(1);
    }
    json.addProperty("source", getObject(source));

    // get breaklines contained by tin geometry
    ArrayList<List<String>> breaklineList = exec.executeQuery1(repo, query.getBreaklinesFromGeom(geometry));

    JsonArray breaklines = new JsonArray();
    for (int i = 0; i < breaklineList.size(); i++) {
      breaklines.add(getObject(breaklineList.get(i).get(0)));
    }
    json.add("Breaklines", breaklines);

    // get other versions of geometry
    ArrayList<List<String>> updateList = exec.executeQuery1(repo, query.getTINUpdates(geometry));

    JsonArray originalOf = new JsonArray();
    JsonArray inputFor = new JsonArray();
    JsonArray outputFrom = new JsonArray();
    ArrayList<String> inputArr = new ArrayList<String>();
    ArrayList<String> outputArr = new ArrayList<String>();
    ArrayList<String> originalArr = new ArrayList<String>();

    // decide, whether geometry is input, output or original in TINUpdate
    for (List<String> triple : updateList) {

      String input = getObject(triple.get(0));
      String output = getObject(triple.get(1));
      String original = getObject(triple.get(2));

      // tin has newer versions
      if (input.equals(geometry) && !inputArr.contains(output)) {
        inputFor.add(output);
        inputArr.add(output);

      // tin has older versions
      } else if (output.equals(geometry) && !outputArr.contains(input)) {
        outputFrom.add(input);
        outputArr.add(input);
      }

      // tin is the original of other tins -> has newer versions
      if (original.equals(geometry) && !originalArr.contains(output)) {
        originalOf.add(output);
        originalArr.add(output);
      }

    }

    json.add("newerVersions", inputFor);
    json.add("olderVersions", outputFrom);
    json.add("originalOf", originalOf);

    // create pretty json output
    Gson gson = new GsonBuilder().setPrettyPrinting().create();
    return gson.toJson(json);
  }

  // @GetMapping(path = "/geometry/infos2/repo/{repo}")
  // @Operation(summary = "Get the dimension of a geometry object")
  // @ApiResponse(responseCode = "200", description = "Successful operation")
  // public String getInfos2(@Parameter(description = "The name of the GraphDB repository.") @PathVariable String repo,
  //     @Parameter(description = "The geometry object IRI.") @RequestParam String geometry) {
  //   ArrayList<List<String>> list = exec.executeQuery1(repo, query.getTINUpdates(geometry));

  //   JsonObject json = new JsonObject();
  //   json.addProperty("id", geometry);
  //   JsonArray originalOf = new JsonArray();
  //   JsonArray inputFor = new JsonArray();
  //   JsonArray outputFrom = new JsonArray();
  //   ArrayList<String> inputArr = new ArrayList<String>();
  //   ArrayList<String> outputArr = new ArrayList<String>();
  //   ArrayList<String> originalArr = new ArrayList<String>();

  //   for (List<String> triple : list) {

  //     String input = getObject(triple.get(0));
  //     String output = getObject(triple.get(1));
  //     String original = getObject(triple.get(2));

  //     if (input.equals(geometry) && !inputArr.contains(output)) {
  //       inputFor.add(output);
  //       inputArr.add(output);
  //     } else if (output.equals(geometry) && !outputArr.contains(input)) {
  //       outputFrom.add(input);
  //       outputArr.add(input);
  //     }

  //     if (original.equals(geometry) && !originalArr.contains(output)) {
  //       originalOf.add(output);
  //       originalArr.add(output);
  //     }

  //   }

  //   json.add("newerVersion", inputFor);
  //   json.add("olderVersion", outputFrom);
  //   json.add("originalOf", originalOf);
  //   return json.toString();
  // }

  // get breaklines contained by tin object
  @GetMapping(path = "/geometry/tinBreaklines/repo/{repo}")
  @Operation(summary = "Get breaklines contained by a tin object")
  @ApiResponse(responseCode = "200", description = "Successful operation")
  public String getBreaklines(@Parameter(description = "The name of the GraphDB repository.") @PathVariable String repo,
      @Parameter(description = "The tin object id.") @RequestParam String geometry) {
    ArrayList<List<String>> list = exec.executeQuery1(repo, query.getBreaklinesFromGeom(geometry));

    JsonArray breaklines = new JsonArray();
    for (int i = 0; i < list.size(); i++) {
      breaklines.add(getObject(list.get(i).get(0)));
    }

    // create pretty json output
    Gson gson = new GsonBuilder().setPrettyPrinting().create();
    return gson.toJson(breaklines);
  }

  // get version and original of tin object for TINUpdate
  @GetMapping(path = "/geometry/tinVersion/repo/{repo}")
  @Operation(summary = "Get info of version and original tin for tin object")
  @ApiResponse(responseCode = "200", description = "Successful operation")
  public String getInfosForTIN(@Parameter(description = "The name of the GraphDB repository.") @PathVariable String repo,
      @Parameter(description = "The tin object id.") @RequestParam String geometry) {
    ArrayList<List<String>> list = exec.executeQuery1(repo, query.getInfosForTINUpdate(geometry));

    JsonObject json = new JsonObject();
    for (int i = 0; i < list.size(); i++) {
      json.addProperty("version", list.get(i).get(0));
      json.addProperty("source", list.get(i).get(1));
      try {
        json.addProperty("original", list.get(i).get(2));
      } catch (Exception e) {
        // if it has no original, because it is self an original, value will be empty
        json.addProperty("original", "");
        log.info("no original found");
      }
    }

    // create pretty json output
    Gson gson = new GsonBuilder().setPrettyPrinting().create();
    return gson.toJson(json);
  }
  // get all version which relate to tin object
  @GetMapping(path = "/geometry/tinAllVersions/repo/{repo}")
  @Operation(summary = "Get all versions of tin object")
  @ApiResponse(responseCode = "200", description = "Successful operation")
  public String getAllVersionsfromTIN(@Parameter(description = "The name of the GraphDB repository.") @PathVariable String repo,
      @Parameter(description = "The tin object id.") @RequestParam String geometry) {
    ArrayList<List<String>> list = exec.executeQuery1(repo, query.getAllVersions(geometry));

    JsonArray jsonArr = new JsonArray();
    for (int i = 0; i < list.size(); i++) {
      JsonObject json = new JsonObject();
      json.addProperty("version", list.get(i).get(0));
      json.addProperty("TIN", getObject(list.get(i).get(1)));
      jsonArr.add(json);
    }

    // create pretty json output
    Gson gson = new GsonBuilder().setPrettyPrinting().create();
    return gson.toJson(jsonArr);
  }

  // get object from iri
  public String getObject(String iri) {
    String[] path = iri.replaceAll("\\#", "\\/").split("\\/");
    return path[path.length - 1];
  }

}