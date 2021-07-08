package com.Microservices.PostgresImportService.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.Microservices.PostgresImportService.repositories.BreaklinesRepository;
import com.Microservices.PostgresImportService.repositories.EmbarkmentRepository;
import com.Microservices.PostgresImportService.repositories.Line2DRepository;
import com.Microservices.PostgresImportService.repositories.Line3DRepository;
import com.Microservices.PostgresImportService.repositories.Point2DRepository;
import com.Microservices.PostgresImportService.repositories.Point3DRepository;
import com.Microservices.PostgresImportService.repositories.Polygon2DRepository;
import com.Microservices.PostgresImportService.repositories.Polygon3DRepository;
import com.Microservices.PostgresImportService.repositories.SolidRepository;
import com.Microservices.PostgresImportService.repositories.SpecialPointsRepository;
import com.Microservices.PostgresImportService.repositories.TINRepository;
import com.Microservices.PostgresImportService.schemas.Breaklines;
import com.Microservices.PostgresImportService.schemas.Embarkment;
import com.Microservices.PostgresImportService.schemas.Line2D;
import com.Microservices.PostgresImportService.schemas.Line3D;
import com.Microservices.PostgresImportService.schemas.Point2D;
import com.Microservices.PostgresImportService.schemas.Point3D;
import com.Microservices.PostgresImportService.schemas.Polygon2D;
import com.Microservices.PostgresImportService.schemas.Polygon3D;
import com.Microservices.PostgresImportService.schemas.Solid;
import com.Microservices.PostgresImportService.schemas.SpecialPoints;
import com.Microservices.PostgresImportService.schemas.TIN;
import com.Microservices.PostgresImportService.service.CheckFiles;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.tags.Tag;

@RefreshScope
@RestController
@Tag(name = "Import", description = "Import semantic data into a GraphDB database")
public class InputController {

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

  Logger log = LoggerFactory.getLogger(InputController.class);

  // get folder and use them
  @GetMapping("/postgresimport/bucket/{bucket}/graphDbRepo/{graphDbRepo}")
  public String send(@PathVariable String bucket, @PathVariable String graphDbRepo) throws Exception {

    log.info("Start import of geometries into postgres database");
    String results = minio.getFiles(bucket, graphDbRepo);

    return results;
  }

  // landing page
  @GetMapping("/")
  public String getIndex() {

    String text = "Hello World!";

    return text;
  }

  // get all collectionId's
  @GetMapping("/collections")
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
  public String getCollectionPage(@PathVariable String collectionId) {

    String collection = tinRepo.getCollection(collectionId);

    return collection;
  }

  // get all items of one collection
  @GetMapping("/collections/{collectionId}/items")
  public String getItemsPage(@PathVariable String collectionId) {

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

    String res = "";
    for (int i = 0; i < items.size(); i++) {
      res += items.get(i) + "\n";
    }

    return "Geometries count: " + items.size() + "\n" + res;
  }

  // get all items of one collection - in standard formats json, xml
  @GetMapping(value = "/collections/{collectionId}/items/{featureId}", produces = { "application/geo+json",
      "application/xml", "application/json" }) // , "text/plain","text/html", "text/csv", "application/yaml"
  public @ResponseBody Object getFeaturePage(@PathVariable String collectionId, @PathVariable UUID featureId) {
    // @RequestHeader(value = "accept") String contentType,
    Object feature = null;

    switch (collectionId) {
      case "point_2d":
        feature = point2dRepo.getItem(featureId);
        break;
      case "point_3d":
        feature = point3dRepo.getItem(featureId);
        break;
      case "line_2d":
        feature = line2dRepo.getItem(featureId);
        break;
      case "line_3d":
        feature = line3dRepo.getItem(featureId);
        break;
      case "polygon_2d":
        feature = poly2dRepo.getItem(featureId);
        break;
      case "polygon_3d":
        feature = poly3dRepo.getItem(featureId);
        break;
      case "solid":
        feature = solidRepo.getItem(featureId);
        break;
      case "dtm_tin":
        feature = tinRepo.getItemTIN(featureId);
        break;
      case "dtm_breaklines":
        feature = blRepo.getItem2(featureId);
        break;
      case "dtm_embarkment":
        feature = embRepo.getItem2(featureId);
        break;
      case "dtm_specialpoints":
        feature = sPntRepo.getItem2(featureId);
        break;
      default:
        break;
    }
    if (feature != null) {
    return feature;
    } else {
    return "CollectionId or featureId does not exist.";
    }
    
  }

  // get all items of one collection - in other formats z.B. plain text
  @GetMapping(value = "/collections/{collectionId}/items/{featureId}", produces = { "text/plain"}) // , "text/html", "text/csv", "application/yaml" 
  public @ResponseBody String getFeaturePage2(@RequestHeader(value = "accept") String contentType,
      @PathVariable String collectionId, @PathVariable UUID featureId) {

    String text = "";

    switch (collectionId) {
      case "point_2d":
        Point2D point2d = point2dRepo.getItem(featureId);
        text = "id: " + point2d.getId() + ", origin_id: " + point2d.getOrigin_id() + ", geometry: "
        + point2d.getGeometry();
        break;
      case "point_3d":
        Point3D point3d = point3dRepo.getItem(featureId);
        text = "id: " + point3d.getId() + ", origin_id: " + point3d.getOrigin_id() + ", geometry: "
            + point3d.getGeometry();
        break;
      case "line_2d":
        Line2D line2d = line2dRepo.getItem(featureId);
        text = "id: " + line2d.getId() + ", origin_id: " + line2d.getOrigin_id() + ", geometry: "
        + line2d.getGeometry();
        break;
      case "line_3d":
        Line3D line3d = line3dRepo.getItem(featureId);
        text = "id: " + line3d.getId() + ", origin_id: " + line3d.getOrigin_id() + ", geometry: "
        + line3d.getGeometry();
        break;
      case "polygon_2d":
        Polygon2D polygon2d = poly2dRepo.getItem(featureId);
        text = "id: " + polygon2d.getId() + ", origin_id: " + polygon2d.getOrigin_id() + ", geometry: "
        + polygon2d.getGeometry();
        break;
      case "polygon_3d":
        Polygon3D polygon3d = poly3dRepo.getItem(featureId);
        text = "id: " + polygon3d.getId() + ", origin_id: " + polygon3d.getOrigin_id() + ", geometry: "
        + polygon3d.getGeometry();
        break;
      case "solid":
        Solid solid = solidRepo.getItem(featureId);
        text = "id: " + solid.getId() + ", origin_id: " + solid.getOrigin_id() + ", geometry: "
        + solid.getGeometry();
        break;
      case "dtm_tin":
        TIN tin = tinRepo.getItemTIN(featureId);
        text = "id: " + tin.getId() + ", geometry: "
        + tin.getGeometry();
        break;
      case "dtm_breaklines":
        Breaklines breaklines = blRepo.getItem2(featureId);
        text = "id: " + breaklines.getId() + ", tin_id: " + breaklines.getTin_id() + ", geometry: "
        + breaklines.getGeometry();
        break;
      case "dtm_embarkment":
        Embarkment embarkment = embRepo.getItem2(featureId);
        text = "id: " + embarkment.getId() + ", tin_id: " + embarkment.getTin_id() + ", geometry: "
        + embarkment.getGeometry();
        break;
      case "dtm_specialpoints":
        SpecialPoints spPoint = sPntRepo.getItem2(featureId);
        text = "id: " + spPoint.getId() + ", tin_id: " + spPoint.getTin_id() + ", geometry: "
        + spPoint.getGeometry();
        break;
      default:
        break;
    }
    return text;
  }

}