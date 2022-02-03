package com.Microservices.GeometryHandler.service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.Microservices.GeometryHandler.connection.FileInputHandlerConnection;
import com.Microservices.GeometryHandler.controller.ExportController;
import com.Microservices.GeometryHandler.domain.model.PostgresInfos;
import com.Microservices.GeometryHandler.domain.model.UpdateJSON;
import com.Microservices.GeometryHandler.repositories.BreaklinesRepository;
import com.Microservices.GeometryHandler.repositories.Polygon3DRepository;
import com.Microservices.GeometryHandler.repositories.TINRepository;
import com.Microservices.GeometryHandler.schemas.Breaklines;
import com.Microservices.GeometryHandler.schemas.Polygon3D;
import com.Microservices.GeometryHandler.schemas.TIN;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.CoordinateList;
import org.locationtech.jts.geom.CoordinateSequence;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryCollection;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LineSegment;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.MultiLineString;
import org.locationtech.jts.geom.Polygon;
import org.locationtech.jts.geom.PrecisionModel;
import org.locationtech.jts.geom.impl.CoordinateArraySequence;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKTReader;
import org.locationtech.jts.io.WKTWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class UpdateTIN {

    @Autowired
    TriangulationHelper helper;

    @Autowired
    TINRepository tinRepository;

    @Autowired
    BreaklinesRepository blRepository;

    @Autowired
    Polygon3DRepository poly3dRepository;

    @Autowired
    ExportController export;

    @Autowired
    FileInputHandlerConnection graphdb;

    @Value("${domain.url}")
    private String domain;

    Logger log = LoggerFactory.getLogger(UpdateTIN.class);

    public String recalculateTIN(String input, String repo) throws ParseException {
        String urlPrefix = domain + "/geometry/export/collections/";
        String result = "";

        // deserialize json string to POJO
        Gson gson = new Gson();
        UpdateJSON[] json = null;
        try {
            json = gson.fromJson(input, UpdateJSON[].class);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }

        GeometryCollection newTIN = new GeometryCollection(new Geometry[0], new GeometryFactory());
        UUID formerFeatureID = null;
        UUID newFeatureID = null;

        // start loop over TIN updates in JSON
        for (int a = 0; a < json.length; a++) {
            PrecisionModel precisionModel = new PrecisionModel(PrecisionModel.FLOATING);
            GeometryFactory factory = new GeometryFactory(precisionModel, json[a].getObjectInfo().getSrid());

            // check, if same TIN as in former loop is used
            // put start TIN to point list
            CoordinateList pointList = new CoordinateList();
            if (json[a].getObjectInfo().getFeatureId().equals(formerFeatureID)) {
                pointList = new CoordinateList(newTIN.getCoordinates(), false);
                formerFeatureID = newFeatureID;
            } else {
                // get original TIN by collectionId and featureId
                TIN dbTin = TIN.class.cast(
                        export.getFeaturePageEpsg("application/json", json[a].getObjectInfo().getCollectionId(),
                                json[a].getObjectInfo().getFeatureId(), json[a].getObjectInfo().getSrid()));

                String[] geom = dbTin.getGeometry().split(";");
                String geometry = geom[1];
                if (geometry.startsWith("TIN")) {
                    geometry = "GEOMETRYCOLLECTION Z("
                            + geometry.replace("TIN(", "").replaceAll("\\(\\(", "POLYGON Z((");
                }

                WKTReader wkt = new WKTReader(factory);
                GeometryCollection oTin = (GeometryCollection) wkt.read(geometry);
                pointList = new CoordinateList(oTin.getCoordinates(), false);
                formerFeatureID = dbTin.getId();

            }
            System.out.println("Starting points: " + pointList.size());

            // read lists from JSON

            // put added points from json into unique list
            List<List<Double>> newPtList = json[a].getAddedPoints();
            CoordinateList addedPoints = new CoordinateList();
            for (List<Double> point : newPtList) {
                Coordinate p = new Coordinate(point.get(0), point.get(1), point.get(2));
                addedPoints.add(p, false);
            }
            System.out.println("Points to add: " + addedPoints.size());

            // put removed points from json into unique list
            List<List<Double>> remPtList = json[a].getRemovedPoints();
            CoordinateList removedPoints = new CoordinateList();
            for (List<Double> point : remPtList) {
                Coordinate p = new Coordinate(point.get(0), point.get(1), point.get(2));
                removedPoints.add(p, false);
            }
            System.out.println("Points to remove: " + removedPoints.size());

            // put breaklines from json into line and line segments list
            List<List<List<Double>>> breaklineList = json[a].getBreaklines();
            boolean breaklines = breaklineList != null && breaklineList.size() > 0;
            LineString[] lineStringList = null;
            ArrayList<LineSegment> lineSegments = new ArrayList<LineSegment>();

            if (breaklines) {
                lineStringList = new LineString[breaklineList.size()];

                for (int f = 0; f < breaklineList.size(); f++) {
                    List<List<Double>> linePoints = breaklineList.get(f);
                    CoordinateList linePointList = new CoordinateList();

                    for (List<Double> coord : linePoints) {
                        Coordinate p = new Coordinate(coord.get(0), coord.get(1), coord.get(2));
                        linePointList.add(p);
                    }

                    for (int i = 0; i < linePointList.size() - 1; i++) {
                        LineSegment segment = new LineSegment(linePointList.get(i), linePointList.get(i + 1));
                        lineSegments.add(segment);
                    }

                    CoordinateSequence cs = new CoordinateArraySequence(linePointList.toCoordinateArray());
                    LineString lineString = new LineString(cs, factory);
                    lineStringList[f] = lineString;
                }
                System.out.println("Added breaklines: " + lineStringList.length);
                System.out.println("Added breakline segments: " + lineSegments.size());

            } else
                System.out.println("No breaklines");
            MultiLineString lineList = factory.createMultiLineString(lineStringList);

            pointList.removeAll(removedPoints);
            System.out.println("Points after removing: " + pointList.size());
            pointList.addAll(addedPoints);
            System.out.println("Points after adding: " + pointList.size());

             // calculate new TIN with conforming delauney triangulation
             GeometryCollection triangles = helper.triangulate(pointList, lineList, factory);

             // filter new calculated points
             CoordinateList newPointList = new CoordinateList(triangles.getCoordinates());
             newPointList.removeAll(pointList);
             System.out.println("New points after triangulation: " + newPointList.size());

            // interpolate all z values of new points and create polygon list
            List<Polygon> polygonList = helper.interpolateNaNValues(triangles, lineSegments, newPointList, breaklines, factory);

            // write polygon list to WKT
            Polygon[] pol = new Polygon[polygonList.size()];
            polygonList.toArray(pol);
            GeometryCollection col = new GeometryCollection(pol, factory);

            WKTWriter wkt = new WKTWriter(3);
            wkt.setPrecisionModel(precisionModel);
            String gcWkt = wkt.write(col);

            // push updated TIN into postgres database
            String tin = "SRID=" + col.getSRID() + ";" + gcWkt.replace("GEOMETRYCOLLECTION Z", "TIN").replaceAll("POLYGON Z","");
            log.info(tin);
            TIN updatedTin = new TIN(tin);
            tinRepository.save(updatedTin);
            log.info("'ID: " + updatedTin.getId() + ", WKT: " + updatedTin.getGeometry() + "'");
            result += updatedTin.getId().toString() + "\n";

            // get version and optional original tin from input tin
            String res = graphdb.graphdbGetTINInfos(formerFeatureID.toString(), repo);
            JsonObject resJson = gson.fromJson(res, JsonObject.class);

            String newVersion = "1.0";
            UUID original = formerFeatureID;
            try {
                char[] v = resJson.get("version").toString().replaceAll("\"", "").toCharArray();

                for (int i = 0; i < v.length; i++) {
                    if (Character.isDigit(v[i])) {
                        int d = Character.getNumericValue(v[i]) + 1;
                        v[i] = Character.forDigit(d, 10);
                        newVersion = new String(v);
                        break;
                    }

                    // if the original is empty, input is also the original tin
                    try {
                        String[] s = resJson.get("original").toString().replaceAll("\"", "").split("\\/");
                        original = UUID.fromString(s[s.length - 1]);
                    } catch (Exception e) {
                        log.error("No original found");
                        // original = formerFeatureID;
                    }

                }
            } catch (Exception e) {
                log.error("No tin for id " + formerFeatureID + " found.");
            }

            // send update information to GraphDB
            String postgresUrl = urlPrefix + "dtm_tin" + "/items/" + updatedTin.getId();
            PostgresInfos p = new PostgresInfos(updatedTin.getId(), formerFeatureID, original, newVersion,
                    json[a].getMetaInfos().getUser(), postgresUrl, 4, 3, repo);
            graphdb.graphdbImport(p);

            String boundary = tinRepository.getExteriorRing(updatedTin.getId());
            Polygon3D poly = new Polygon3D(-1, boundary);
            poly3dRepository.save(poly);
            String postgresUrlBoundary = urlPrefix + "polygon_3d" + "/items/" + poly.getId();
            PostgresInfos pBoundary = new PostgresInfos(poly.getId(), postgresUrlBoundary, 2, 3, repo, "bounds",
                    updatedTin.getId());
            graphdb.graphdbImport(pBoundary);

            if (breaklines) {
                for (int i = 0; i < lineStringList.length; i++) {
                    String l = wkt.write(lineStringList[i]);
                    Breaklines bl = new Breaklines(updatedTin.getId(),
                            "SRID=" + json[a].getObjectInfo().getSrid() + ";" + l);
                    blRepository.save(bl);
                    log.info("'ID: " + bl.getId() + ", WKT: " + bl.getGeometry() + ", tin_id: " + bl.getTin_id() + "'");

                    String postgresUrlBl = urlPrefix + "dtm_breaklines" + "/items/" + bl.getId();
                    PostgresInfos pBl = new PostgresInfos(bl.getId(), postgresUrlBl, 1, 3, repo, "breaklineOf",
                            updatedTin.getId());
                    graphdb.graphdbImport(pBl);
                }
            }

            newTIN = col;
            newFeatureID = updatedTin.getId();
            formerFeatureID = json[a].getObjectInfo().getFeatureId();
        }
        return result;

    }

}
