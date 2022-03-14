package com.Microservices.BIMserverQueryService.controller;

import java.util.Optional;

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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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
  @Operation(summary = "Get all walls in ifc model including elements of subclasses")
  public ResponseEntity<Resource> getAllWalls(@RequestParam String projectName,
      @Parameter(description = "Include the structure, where elements are contained in, the owner history, the geometry representation and the object placement.") @RequestParam boolean basics) {

    BimServerClient client = bimserver.getConnection();
    long[] pInfos = func.getProjectRoidAndSerializerOid(client, projectName);

    return func.getFileByQuery(client, pInfos[0], pInfos[1], queries.getWalls(basics));
  }

  @GetMapping("/querybimserver/getExternalWalls")
  @Operation(summary = "Get all external walls off ifc model")
  public ResponseEntity<Resource> getExternalWalls(@RequestParam String projectName,
      @Parameter(description = "Include the structure, where elements are contained in, the owner history, the geometry representation and the object placement.") @RequestParam boolean basics) {

    BimServerClient client = bimserver.getConnection();
    long[] pInfos = func.getProjectRoidAndSerializerOid(client, projectName);

    return func.getFileByQuery(client, pInfos[0], pInfos[1], queries.getExternalWalls(basics));
  }

  @GetMapping("/querybimserver/getWallsInStorey")
  @Operation(summary = "Get all walls in a specific storey")
  public ResponseEntity<Resource> getWallsInStorey(@RequestParam String projectName, @RequestParam String guid) {

    BimServerClient client = bimserver.getConnection();
    long[] pInfos = func.getProjectRoidAndSerializerOid(client, projectName);

    return func.getFileByQuery(client, pInfos[0], pInfos[1], queries.getStoreyWalls(guid));
  }

  @GetMapping("/querybimserver/getElements")
  @Operation(summary = "Get a list of elements from different ifc classes")
  public ResponseEntity<Resource> getElements(@RequestParam String projectName,
      @Parameter(description = "Comma seperated list of ifc element classes") @RequestParam String elementClasses,
      @Parameter(description = "Include the structure, where elements are contained in, the owner history, the geometry representation and the object placement.") @RequestParam boolean basics) {

    BimServerClient client = bimserver.getConnection();
    long[] pInfos = func.getProjectRoidAndSerializerOid(client, projectName);

    return func.getFileByQuery(client, pInfos[0], pInfos[1], queries.getElements(elementClasses, basics));
  }

  // @GetMapping("/querybimserver/getInBoundingBox")
  // @Operation(summary = "Get all Entities in a bounding box.", description = "
  // The unit of the values depends on the length unit in the ifc model")
  // public ResponseEntity<Resource> getInBoundingBox(@RequestParam String
  // projectName, @RequestParam double x,
  // @RequestParam double y, @RequestParam double z, @RequestParam double width,
  // @RequestParam double height,
  // @RequestParam double depth, @RequestParam boolean partial,
  // @Parameter(description = "Include the structure, where elements are contained
  // in, the owner history, the geometry representation and the object
  // placement.") @RequestParam boolean basics) {

  // BimServerClient client = bimserver.getConnection();
  // long[] pInfos = func.getProjectRoidAndSerializerOid(client, projectName);

  // return func.getFileByQuery(client, pInfos[0], pInfos[1],
  // queries.getInBoundingBox(x, y, z, width, height, depth, partial, basics));
  // }

  @GetMapping("/querybimserver/getElementsInBoundingBox")
  @Operation(summary = "Get all Entities in a bounding box.", description = " The unit of the values depends on the length unit in the ifc model")
  public ResponseEntity<Resource> getElementsInBoundingBox(@RequestParam String projectName,
      @Parameter(description = "Comma seperated list of ifc element classes") @RequestParam Optional<String> elementClasses,
      @RequestParam double x,
      @RequestParam double y, @RequestParam double z, @RequestParam double width, @RequestParam double height,
      @RequestParam double depth, @RequestParam boolean partial,
      @Parameter(description = "Include the structure, where elements are contained in, the owner history, the geometry representation and the object placement.") @RequestParam boolean basics) {

    BimServerClient client = bimserver.getConnection();
    long[] pInfos = func.getProjectRoidAndSerializerOid(client, projectName);
    String elements = null;
    if (elementClasses.isPresent()) {
      elements = elementClasses.get();
    }

    return func.getFileByQuery(client, pInfos[0], pInfos[1],
        queries.getElementsInBoundingBox(x, y, z, width, height, depth, partial, elements, basics));
  }

  @GetMapping("/querybimserver/getElementsByProperty")
  @Operation(summary = "Get all elements with specific property in property set")
  public ResponseEntity<Resource> getElementsByProperty(@RequestParam String projectName,
      @RequestParam Optional<String> types, @RequestParam String propertySet, @RequestParam String property,
      @RequestParam String value,
      @Parameter(description = "Include the structure, where elements are contained in, the owner history, the geometry representation and the object placement.") @RequestParam boolean basics) {

    BimServerClient client = bimserver.getConnection();
    long[] pInfos = func.getProjectRoidAndSerializerOid(client, projectName);
    String elementTypes = null;
    if (types.isPresent()) {
      elementTypes = types.get();
    }
    return func.getFileByQuery(client, pInfos[0], pInfos[1],
        queries.getElementsByProperty(elementTypes, propertySet, property, value, basics));
  }

  @GetMapping("/querybimserver/getPropertiesFromElements")
  @Operation(summary = "Get all properties of specific elements")
  public ResponseEntity<Resource> getPropertiesFromElements(@RequestParam String projectName,
      @Parameter(description = "Comma seperated list of ifc element guids") @RequestParam String guids,
      @Parameter(description = "Include the structure, where elements are contained in, the owner history, the geometry representation and the object placement.") @RequestParam(defaultValue = "true") boolean basics) {

    BimServerClient client = bimserver.getConnection();
    long[] pInfos = func.getProjectRoidAndSerializerOid(client, projectName);

    return func.getFileByQuery(client, pInfos[0], pInfos[1], queries.getPropertiesFromElements(guids, basics));
  }

  @GetMapping("/querybimserver/getWallsByType")
  @Operation(summary = "Get all walls with specific wall type")
  public ResponseEntity<Resource> getWallsByType(@RequestParam String projectName,
      @Parameter(description = "Comma seperated list of ifc wall type guids") @RequestParam String guids,
      @Parameter(description = "Include the structure, where elements are contained in, the owner history, the geometry representation and the object placement.") @RequestParam boolean basics) {

    BimServerClient client = bimserver.getConnection();
    long[] pInfos = func.getProjectRoidAndSerializerOid(client, projectName);

    return func.getFileByQuery(client, pInfos[0], pInfos[1], queries.getWallsByType(guids, basics));
  }

}
