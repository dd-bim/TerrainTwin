package com.Microservices.TopologyService.controller;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import com.Microservices.TopologyService.repositories.Point2DRepository;
import com.Microservices.TopologyService.repositories.Polygon2DRepository;
import com.Microservices.TopologyService.repositories.TINRepository;
import com.Microservices.TopologyService.schemas.Polygon2D;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.tags.Tag;

@RefreshScope
@RestController
@Tag(name = "Topology Service", description = "Compute relationships between geometries from Postgres")
public class ImportRestController {

  // @Autowired
  // CheckFiles minio;

  @Autowired
  Point2DRepository point2dRepo;

  @Autowired
  Polygon2DRepository polygon2dRepo;

  @Autowired
  TINRepository tinRepository;

  Logger log = LoggerFactory.getLogger(ImportRestController.class);

  // // get folder and use them
  // @GetMapping("/postgresimport/bucket/{bucket}/graphDbRepo/{graphDbRepo}")
  // public String send(@PathVariable String bucket, @PathVariable String graphDbRepo) throws Exception {

  //   log.info("Start import of geometries into postgres database");
  //   String results = minio.getFiles(bucket, graphDbRepo);

  //   return results;
  // }

  //   // get folder and use them
  //   @GetMapping("/postgresexport/DGM/id/{id}")
  //   public String getDGM(@PathVariable UUID id) throws Exception {
  
  //   String geom = tinRepository.getTINGeometry(id);

  //     return geom;
  //   }

    @GetMapping("/topology/intersect")
    public String intersect() {
  String intersections = "";
    // HashMap<UUID,UUID> intersects = point2dRepo.getIntersects();

    // for(UUID key : intersects.keySet()) {
    //   intersections += key + ", " + intersects.get(key) + "\n";
    // }

    // String [][] intersects = point2dRepo.getIntersects();
    // for (int i = 0; i < intersects.length; i++) {

    //   intersections += intersects[i][0] + ", " + intersects[i][1] + "\n";

    // }

    // List<Polygon2D> poly = (List<Polygon2D>) polygon2dRepo.findAll();
    // for (int i = 0; i < poly.size(); i++) {
    // String [] intersects = point2dRepo.getIntersects(poly.get(i).getGeometry());
    // for (int j = 0; j < intersects.length; j++) {

    //   intersections += intersects[j] + ", " + poly.get(i).getId() + "\n";

    // } 
  // }
  UUID [] intersects = point2dRepo.getIntersects();
    for (int j = 0; j < intersects.length; j++) {

      intersections += intersects[j]  + "\n";

    } 
  // UUID [] intersects = point2dRepo.getT();
  //   for (int j = 0; j < intersects.length; j++) {

  //     intersections += intersects[j]  + "\n";

  //   } 
  // Integer i = polygon2dRepo.getT();
  // intersections = tinRepository.getTINGeometry();
;      return intersections;
    }
}