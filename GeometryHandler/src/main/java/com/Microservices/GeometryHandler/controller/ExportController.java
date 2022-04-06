package com.Microservices.GeometryHandler.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.Microservices.GeometryHandler.repositories.BreaklinesRepository;
import com.Microservices.GeometryHandler.repositories.EmbarkmentRepository;
import com.Microservices.GeometryHandler.repositories.Line2DRepository;
import com.Microservices.GeometryHandler.repositories.Line3DRepository;
import com.Microservices.GeometryHandler.repositories.Point2DRepository;
import com.Microservices.GeometryHandler.repositories.Point3DRepository;
import com.Microservices.GeometryHandler.repositories.Polygon2DRepository;
import com.Microservices.GeometryHandler.repositories.Polygon3DRepository;
import com.Microservices.GeometryHandler.repositories.SolidRepository;
import com.Microservices.GeometryHandler.repositories.SpecialPointsRepository;
import com.Microservices.GeometryHandler.repositories.TINRepository;
import com.Microservices.GeometryHandler.schemas.Breaklines;
import com.Microservices.GeometryHandler.schemas.Embarkment;
import com.Microservices.GeometryHandler.schemas.Line2D;
import com.Microservices.GeometryHandler.schemas.Line3D;
import com.Microservices.GeometryHandler.schemas.Point2D;
import com.Microservices.GeometryHandler.schemas.Point3D;
import com.Microservices.GeometryHandler.schemas.Polygon2D;
import com.Microservices.GeometryHandler.schemas.Polygon3D;
import com.Microservices.GeometryHandler.schemas.Solid;
import com.Microservices.GeometryHandler.schemas.SpecialPoints;
import com.Microservices.GeometryHandler.schemas.TIN;
import com.Microservices.GeometryHandler.service.CheckFiles;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@RefreshScope
@RestController
@Tag(name = "Export")
@RequestMapping("/geometry/export")
public class ExportController {

  @Autowired
  CheckFiles minio;

  @Autowired
  Point2DRepository point2dRepo;

  @Autowired
  Point3DRepository point3dRepo;

  @Autowired
  Line2DRepository line2dRepo;

  @Autowired
  Line3DRepository line3dRepo;

  @Autowired
  Polygon2DRepository poly2dRepo;

  @Autowired
  Polygon3DRepository poly3dRepo;

  @Autowired
  SolidRepository solidRepo;

  @Autowired
  TINRepository tinRepo;

  @Autowired
  BreaklinesRepository blRepo;

  @Autowired
  EmbarkmentRepository embRepo;

  @Autowired
  SpecialPointsRepository sPntRepo;

  Logger log = LoggerFactory.getLogger(ExportController.class);

  // public static final String exapmleFeature = "{\r\n \"id\":\"string\",\r\n
  // \"origin_id\":0,\r\n \"geometry\": \"string\"\r\n}";

  // landing page
  @GetMapping("/")
  @Operation(summary = "Landing page")
  @ApiResponse(responseCode = "200", description = "Successful operation")
  public String getIndex() {

    String text = "Hello World!";

    return text;
  }

  // conformance page
  @GetMapping("/conformance")
  @Operation(summary = "Get conformance information")
  @ApiResponse(responseCode = "200", description = "Successful operation")
  public String getConformance() {

    String text = "Hello World!";

    return text;
  }

  // get all collectionId's
  @GetMapping("/collections")
  @Operation(summary = "Get all collectionId's")
  @ApiResponse(responseCode = "200", description = "Successful operation")
  public String getCollectionsPage() {

    List<String> collections = tinRepo.getCollections();

    String res = "";
    for (int i = 0; i < collections.size(); i++) {
      res += collections.get(i) + "\n";
    }

    return res;
  }

  // get one collection by id
  @GetMapping("/collections/{collectionId}")
  @Operation(summary = "Get collection by id")
  @ApiResponse(responseCode = "200", description = "Successful operation")
  public String getCollectionPage(
      @Parameter(description = "The unique id of the collection.") @PathVariable String collectionId) {

    String collection = tinRepo.getCollection(collectionId);

    return collection;
  }

  // get all items of one collection
  @GetMapping("/collections/{collectionId}/items")
  @Operation(summary = "Get all items of a collection")
  @ApiResponse(responseCode = "200", description = "Successful operation")
  public String getItemsPage(
      @Parameter(description = "The unique id of the collection.") @PathVariable String collectionId) {

    List<String> items = new ArrayList<String>();

    switch (collectionId) {
      case "point_2d":
        items = point2dRepo.getItems();
        break;
      case "point_3d":
        items = point3dRepo.getItems();
        break;
      case "line_2d":
        items = line2dRepo.getItems();
        break;
      case "line_3d":
        items = line3dRepo.getItems();
        break;
      case "polygon_2d":
        items = poly2dRepo.getItems();
        break;
      case "polygon_3d":
        items = poly3dRepo.getItems();
        break;
      case "solid":
        items = solidRepo.getItems();
        break;
      case "dtm_tin":
        items = tinRepo.getItems();
        break;
      case "dtm_breaklines":
        items = blRepo.getItems();
        break;
      case "dtm_embarkment":
        items = embRepo.getItems();
        break;
      case "dtm_specialpoints":
        items = sPntRepo.getItems();
        break;
      default:
        break;
    }

    JsonArray json = new JsonArray();
    items.forEach(item -> {
      json.add(item);
    });

    Gson gson = new GsonBuilder().setPrettyPrinting().create();
    return gson.toJson(json);
  }

  // get all items of one collection - in standard formats json, xml
  @GetMapping(value = "/collections/{collectionId}/items/{featureId}", produces = { "application/json",
      "application/xml", "text/plain" }) // , "text/html", "text/csv",
  // "application/yaml", "application/geo+json"
  @Operation(summary = "Export feature by id")
  @ApiResponse(responseCode = "200", description = "Successful operation") // , content = @Content(examples =
                                                                           // @ExampleObject(value = exapmleFeature)))
  public @ResponseBody Object getFeaturePage(
      @Parameter(hidden = true) @RequestHeader(value = "accept") String contentType,
      @Parameter(description = "The unique id of the collection.") @PathVariable String collectionId,
      @Parameter(description = "The unique id of the geometry.") @PathVariable UUID featureId) {

    Object feature = null;
    String text = "";
    try {
      switch (collectionId) {
        case "point_2d":
          Point2D point2d = point2dRepo.getItem(featureId);
          text = "id: " + point2d.getId() + ", origin_id: " + point2d.getOrigin_id() + ", geometry: "
              + point2d.getGeometry();
          feature = point2d;
          break;
        case "point_3d":
          Point3D point3d = point3dRepo.getItem(featureId);
          text = "id: " + point3d.getId() + ", origin_id: " + point3d.getOrigin_id() + ", geometry: "
              + point3d.getGeometry();
          feature = point3d;
          break;
        case "line_2d":
          Line2D line2d = line2dRepo.getItem(featureId);
          text = "id: " + line2d.getId() + ", origin_id: " + line2d.getOrigin_id() + ", geometry: "
              + line2d.getGeometry();
          feature = line2d;
          break;
        case "line_3d":
          Line3D line3d = line3dRepo.getItem(featureId);
          text = "id: " + line3d.getId() + ", origin_id: " + line3d.getOrigin_id() + ", geometry: "
              + line3d.getGeometry();
          feature = line3d;
          break;
        case "polygon_2d":
          Polygon2D polygon2d = poly2dRepo.getItem(featureId);
          text = "id: " + polygon2d.getId() + ", origin_id: " + polygon2d.getOrigin_id() + ", geometry: "
              + polygon2d.getGeometry();
          feature = polygon2d;
          break;
        case "polygon_3d":
          Polygon3D polygon3d = poly3dRepo.getItem(featureId);
          text = "id: " + polygon3d.getId() + ", origin_id: " + polygon3d.getOrigin_id() + ", geometry: "
              + polygon3d.getGeometry();
          feature = polygon3d;
          break;
        case "solid":
          Solid solid = solidRepo.getItem(featureId);
          text = "id: " + solid.getId() + ", origin_id: " + solid.getOrigin_id() + ", geometry: " + solid.getGeometry();
          feature = solid;
          break;
        case "dtm_tin":
          TIN tin = tinRepo.getItemTIN(featureId);
          text = "id: " + tin.getId() + ", geometry: " + tin.getGeometry();
          feature = tin;
          break;
        case "dtm_breaklines":
          Breaklines breaklines = blRepo.getItem2(featureId);
          text = "id: " + breaklines.getId() + ", tin_id: " + breaklines.getTin_id() + ", geometry: "
              + breaklines.getGeometry();
          feature = breaklines;
          break;
        case "dtm_embarkment":
          Embarkment embarkment = embRepo.getItem2(featureId);
          text = "id: " + embarkment.getId() + ", tin_id: " + embarkment.getTin_id() + ", geometry: "
              + embarkment.getGeometry();
          feature = embarkment;
          break;
        case "dtm_specialpoints":
          SpecialPoints spPoint = sPntRepo.getItem2(featureId);
          text = "id: " + spPoint.getId() + ", tin_id: " + spPoint.getTin_id() + ", geometry: " + spPoint.getGeometry();
          feature = spPoint;
          break;
        default:
          break;
      }
    } catch (Exception e) {
      text = "CollectionId or featureId does not exist.";
    }

    if (contentType.equals("text/plain")) {
      return text;
    } else if (feature != null) {
      return feature;
    } else {
      return text;
    }
  }

  // get all items of one collection - in standard formats json, xml
  @GetMapping(value = "/collections/{collectionId}/items/{featureId}/epsg/{epsg}", produces = { "application/json",
      "application/xml", "text/plain" }) // , "text/html", "text/csv",
  // "application/yaml", "application/geo+json"
  @Operation(summary = "Export feature by id in the specified spatial reference system")
  @ApiResponse(responseCode = "200", description = "Successful operation") // , content = @Content(examples =
  // @ExampleObject(value = exapmleFeature)))
  public @ResponseBody Object getFeaturePageEpsg(
      @Parameter(hidden = true) @RequestHeader(value = "accept") String contentType,
      @Parameter(description = "The unique id of the collection.") @PathVariable String collectionId,
      @Parameter(description = "The unique id of the geometry.") @PathVariable UUID featureId,
      @Parameter(description = "The epsg code of the spatial reference system.") @PathVariable int epsg) {

    Object feature = null;
    String text = "";
    try {
      switch (collectionId) {
        case "point_2d":
          Point2D point2d = point2dRepo.getItemEpsg(featureId, epsg);
          text = "id: " + point2d.getId() + ", origin_id: " + point2d.getOrigin_id() + ", geometry: "
              + point2d.getGeometry();
          feature = point2d;
          break;
        case "point_3d":
          Point3D point3d = point3dRepo.getItemEpsg(featureId, epsg);
          text = "id: " + point3d.getId() + ", origin_id: " + point3d.getOrigin_id() + ", geometry: "
              + point3d.getGeometry();
          feature = point3d;
          break;
        case "line_2d":
          Line2D line2d = line2dRepo.getItemEpsg(featureId, epsg);
          text = "id: " + line2d.getId() + ", origin_id: " + line2d.getOrigin_id() + ", geometry: "
              + line2d.getGeometry();
          feature = line2d;
          break;
        case "line_3d":
          Line3D line3d = line3dRepo.getItemEpsg(featureId, epsg);
          text = "id: " + line3d.getId() + ", origin_id: " + line3d.getOrigin_id() + ", geometry: "
              + line3d.getGeometry();
          feature = line3d;
          break;
        case "polygon_2d":
          Polygon2D polygon2d = poly2dRepo.getItemEpsg(featureId, epsg);
          text = "id: " + polygon2d.getId() + ", origin_id: " + polygon2d.getOrigin_id() + ", geometry: "
              + polygon2d.getGeometry();
          feature = polygon2d;
          break;
        case "polygon_3d":
          Polygon3D polygon3d = poly3dRepo.getItemEpsg(featureId, epsg);
          text = "id: " + polygon3d.getId() + ", origin_id: " + polygon3d.getOrigin_id() + ", geometry: "
              + polygon3d.getGeometry();
          feature = polygon3d;
          break;
        case "solid":
          Solid solid = solidRepo.getItemEpsg(featureId, epsg);
          text = "id: " + solid.getId() + ", origin_id: " + solid.getOrigin_id() + ", geometry: " + solid.getGeometry();
          feature = solid;
          break;
        case "dtm_tin":
          TIN tin = tinRepo.getItemTINEpsg(featureId, epsg);
          text = "id: " + tin.getId() + ", geometry: " + tin.getGeometry();
          feature = tin;
          break;
        case "dtm_breaklines":
          Breaklines breaklines = blRepo.getItemEpsg2(featureId, epsg);
          text = "id: " + breaklines.getId() + ", tin_id: " + breaklines.getTin_id() + ", geometry: "
              + breaklines.getGeometry();
          feature = breaklines;
          break;
        case "dtm_embarkment":
          Embarkment embarkment = embRepo.getItemEpsg2(featureId, epsg);
          text = "id: " + embarkment.getId() + ", tin_id: " + embarkment.getTin_id() + ", geometry: "
              + embarkment.getGeometry();
          feature = embarkment;
          break;
        case "dtm_specialpoints":
          SpecialPoints spPoint = sPntRepo.getItemEpsg2(featureId, epsg);
          text = "id: " + spPoint.getId() + ", tin_id: " + spPoint.getTin_id() + ", geometry: " + spPoint.getGeometry();
          feature = spPoint;
          break;
        default:
          break;
      }
    } catch (Exception e) {
      text = "CollectionId or featureId does not exist.";
    }

    if (contentType.equals("text/plain")) {
      return text;
    } else if (feature != null) {
      return feature;
    } else {
      return text;
    }

  }

}