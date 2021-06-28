package com.Microservices.TopologyService.controller;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;

import com.Microservices.TopologyService.repositories.BreaklinesRepository;
import com.Microservices.TopologyService.repositories.EmbarkmentRepository;
import com.Microservices.TopologyService.repositories.Line2DRepository;
import com.Microservices.TopologyService.repositories.Line3DRepository;
import com.Microservices.TopologyService.repositories.Point2DRepository;
import com.Microservices.TopologyService.repositories.Point3DRepository;
import com.Microservices.TopologyService.repositories.Polygon2DRepository;
import com.Microservices.TopologyService.repositories.Polygon3DRepository;
import com.Microservices.TopologyService.repositories.SpecialPointsRepository;
import com.Microservices.TopologyService.repositories.TINRepository;
import com.Microservices.TopologyService.service.GetRelationsService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.data.repository.Repository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.tags.Tag;

@RefreshScope
@RestController
@Tag(name = "Topology Service", description = "Compute relationships between geometries from Postgres")
public class ImportRestController {

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
  TINRepository tinRepo;

  @Autowired
  BreaklinesRepository blRepo;

  @Autowired
  EmbarkmentRepository embRepo;

  @Autowired
  SpecialPointsRepository sPntRepo;

  Logger log = LoggerFactory.getLogger(ImportRestController.class);

  @GetMapping("/topology/relations")
  public String relations() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
    long start = System.currentTimeMillis();

    String result = "";
    ArrayList<String[]> relations = new ArrayList<String[]>();

    // iterate over all repository classes and execute defined methods on there
    // instances
    Class[] classes = { Point2DRepository.class, Point3DRepository.class, Line2DRepository.class,
        Line3DRepository.class, Polygon2DRepository.class, Polygon3DRepository.class, TINRepository.class,
        BreaklinesRepository.class, EmbarkmentRepository.class, SpecialPointsRepository.class };
    Repository[] objects = { point2dRepo, point3dRepo, line2dRepo, line3dRepo, poly2dRepo, poly3dRepo, tinRepo, blRepo,
        embRepo, sPntRepo };

    try {
      for (int i = 0; i < classes.length; i++) {

        // get all methods of the repository class starting with "relate"
        Method[] allMethods = classes[i].getMethods();

        // get only needed methods and order them, because order matters in next step
        HashMap<Integer, Method> methods = new HashMap<Integer, Method>();
        for (int j = 0; j < allMethods.length; j++) {
          String methodName = allMethods[j].getName();
          switch (methodName) {
            case "relatePoint2d":
              methods.put(0, allMethods[j]);
            case "relatePoint3d":
              methods.put(1, allMethods[j]);
            case "relateLine2d":
              methods.put(2, allMethods[j]);
            case "relateLine3d":
              methods.put(3, allMethods[j]);
            case "relatePolygon2d":
              methods.put(4, allMethods[j]);
            case "relatePolygon3d":
              methods.put(5, allMethods[j]);
              // case "relateSolid":
              // methods.put(6, allMethods[j]);
            case "relateTIN":
              methods.put(6, allMethods[j]);
            case "relateBreaklines":
              methods.put(7, allMethods[j]);
            case "relateEmbarkment":
              methods.put(8, allMethods[j]);
            case "relateSpecialPoints":
              methods.put(9, allMethods[j]);
            default:
              break;
          }
        }

        // index = i: to avoid duplicate relation generation, corresponding methods to
        // repositories aren't used after the repository is processed
        for (int index = i; index < methods.size(); index++) {
          log.info(methods.get(index).getName());
          String[][] rels = (String[][]) methods.get(index).invoke(objects[i]);
          relations.addAll(GetRelationsService.matchRelations(rels));
        }
      }
      result = "All relations processed. \n";

      // print methods to http response
      for (int z = 0; z < relations.size(); z++) {
        result += relations.get(z)[0] + ", " + relations.get(z)[1] + ", " + relations.get(z)[2] + "\n";
      }

    } catch (Exception e) {
      result = "Something goes wrong. Message: " + e.getMessage();
    }
    long end = System.currentTimeMillis();
    String diff = (end - start) / 1000 + "s";

    return result + "\nProcessed relations: " + relations.size() + "\n" + "Time: " + diff;
  }
}