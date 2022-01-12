package com.Microservices.GeometryHandler.controller;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import com.Microservices.GeometryHandler.domain.model.UpdateJSON;
import com.Microservices.GeometryHandler.repositories.TINRepository;
import com.Microservices.GeometryHandler.schemas.TIN;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryCollection;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Polygon;
import org.locationtech.jts.geom.PrecisionModel;
import org.locationtech.jts.triangulate.DelaunayTriangulationBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@RefreshScope
@RestController
@Tag(name = "Edit geometries")
@RequestMapping("/geometry/edit")
public class EditController {

  @Autowired
  TINRepository tinRepository;

  @Autowired
  ExportController export;

  Logger log = LoggerFactory.getLogger(EditController.class);

  // recalculate TIN with added, removed and changed points
  @PostMapping("/updateTIN")
  @Operation(summary = "Create an updated TIN with update information as JSON")
  @ApiResponse(responseCode = "200", description = "Successful operation")
  public String updateTin(@Parameter(description = "The JSON vor update.") @RequestBody String input) { // UpdateJSON
                                                                                                        // json
    // deserialize json string to POJO
    Gson gson = new Gson();
    
    Type collectionType = new TypeToken<Collection<UpdateJSON>>(){}.getType();
    Collection<UpdateJSON> jList = gson.fromJson(input, collectionType);

    UpdateJSON json = new UpdateJSON();
    // UpdateJSON json = gson.fromJson(input, UpdateJSON.class);

    // iterate over list of TINs
    Iterator<UpdateJSON> iter = jList.iterator();
    int o = 0;                                        // erstmal nur 1 TIN
    while (o < 1) {
    json = iter.next();
    o++;
    }

    // put added points from json into unique list
    List<List<Double>> newPtList = json.getAddedPoints();
    HashSet<Coordinate> addedPoints = new HashSet<Coordinate>();
    for (List<Double> point : newPtList) {
      Coordinate p = new Coordinate(point.get(0), point.get(1), point.get(2));
      addedPoints.add(p);
    }
    log.info("Added points: " + addedPoints.size());

    // put removed points from json into unique list
    List<List<Double>> remPtList = json.getRemovedPoints();
    HashSet<Coordinate> removedPoints = new HashSet<Coordinate>();
    for (List<Double> point : remPtList) {
      Coordinate p = new Coordinate(point.get(0), point.get(1), point.get(2));
      removedPoints.add(p);
    }
    log.info("Removed points: " + removedPoints.size());
    
    // get original TIN by collectionId and featureId
    TIN tin = TIN.class.cast(
        export.getFeaturePageEpsg("application/json", json.getObjectInfo().getCollectionId(), json.getObjectInfo().getFeatureId(), json.getMetaInfos().getSrid()));

    // put points from original TIN into unique list
    String[] lines = tin.getGeometry().split(";");
    String strPoints = lines[1].replaceAll("[a-zA-Z()]", "");
    String[] points = strPoints.split(",");
    HashSet<Coordinate> pointsSet = new HashSet<Coordinate>();

    for (String line : points) {
      String[] coord = line.split(" ");
      Coordinate p = new Coordinate(Double.parseDouble(coord[0]), Double.parseDouble(coord[1]), Double.parseDouble(coord[2]));
      pointsSet.add(p);
    }
    log.info("Original Points: " + pointsSet.size());

    // update original point list with added and removed points
    pointsSet.removeAll(removedPoints);
    log.info("Points after removing: " + pointsSet.size());
    pointsSet.addAll(addedPoints);
    log.info("Points after adding: " + pointsSet.size());

    // calculate new TIN from updated point list
    PrecisionModel precisionModel = new PrecisionModel(PrecisionModel.FLOATING);
    GeometryFactory factory = new GeometryFactory(precisionModel, json.getMetaInfos().getSrid());
    DelaunayTriangulationBuilder builder = new DelaunayTriangulationBuilder();
    builder.setSites(pointsSet);
    Geometry triangles = builder.getTriangles(factory);
    List<Polygon> polygons = new ArrayList<Polygon>();

    // create EWKT from new TIN
    String newTin = "SRID=";
    if(triangles instanceof GeometryCollection) {
        GeometryCollection geomColl = (GeometryCollection) triangles;
        // System.out.println(geomColl.toText());
        newTin += geomColl.getSRID() + ";TIN(";
        for(int i = 0; i < geomColl.getNumGeometries(); i++) {
            Polygon poly = (Polygon) geomColl.getGeometryN(i);
            polygons.add(poly);
            // System.out.println(poly.toText());
            Coordinate[] coordList = poly.getCoordinates();
            newTin += "((";
            int z = 0;
            for( Coordinate c : coordList ){
                newTin += c.getX() + " " + c.getY() + " " + c.getZ();
                if (z < coordList.length -1) newTin += ",";
                z++;
            }
            if (i < geomColl.getNumGeometries() -1) {
                newTin += ")),";
            } else {
                newTin += ")))";
            }
            // System.out.println(coordList[0].getX() + " " + coordList[0].getY() + " " + coordList[0].getZ());
            // System.out.println(poly.getSRID());
        }
        System.out.println(tin);

    }


    // push updated TIN into postgres database
    TIN updatedTin = new TIN(newTin);
    tinRepository.save(updatedTin);
    log.info("'ID: " + updatedTin.getId() + ", WKT: " + updatedTin.getGeometry() + "'");

    // send update information to GraphDB
    // String postgresUrl = urlPrefix + "dtm_tin" + "/items/" + tin.getId();
    // PostgresInfos p = new PostgresInfos(-1, tin.getId(), postgresUrl, 4, 3, filename, path, graphdbRepo);
    // graphdb.graphdbImport(p);

    return updatedTin.getId().toString();
  }
}