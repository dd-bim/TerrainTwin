package com.Microservices.GeometryHandler.service;

import java.util.ArrayList;
import java.util.HashSet;
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
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryCollection;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LineSegment;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.MultiLineString;
import org.locationtech.jts.geom.MultiPoint;
import org.locationtech.jts.geom.Polygon;
import org.locationtech.jts.geom.PrecisionModel;
import org.locationtech.jts.io.WKTWriter;
import org.locationtech.jts.triangulate.ConformingDelaunayTriangulationBuilder;
import org.locationtech.jts.triangulate.quadedge.Vertex;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class UpdateTIN {

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

    public String recalculateTIN(String input, String repo) {
        String urlPrefix = domain + "/geometry/export/collections/";
        String result = "";
        // String repo = "bin";

        // deserialize json string to POJO
        Gson gson = new Gson();
        UpdateJSON[] json = null;
        try {
            json = gson.fromJson(input, UpdateJSON[].class);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }

        String newTIN = null;
        UUID formerFeatureID = null;
        UUID newFeatureID = null;

        // start loop over TIN updates in JSON
        for (int a = 0; a < json.length; a++) {
            PrecisionModel precisionModel = new PrecisionModel(PrecisionModel.FLOATING);
            GeometryFactory factory = new GeometryFactory(precisionModel, 25832);

            // check, if same TIN as in former loop is used
            String strPoints = "";
            if (json[a].getObjectInfo().getFeatureId().equals(formerFeatureID)) {
                // if featureId is same as in former loop, use already updated TIN
                String[] lines = newTIN.split(";");
                strPoints = lines[1].replaceAll("[a-zA-Z()]", "");
                formerFeatureID = newFeatureID;
            } else {
                // get original TIN by collectionId and featureId
                TIN oTin = TIN.class.cast(
                        export.getFeaturePageEpsg("application/json", json[a].getObjectInfo().getCollectionId(),
                                json[a].getObjectInfo().getFeatureId(), json[a].getObjectInfo().getSrid()));

                // put points from original TIN into unique list
                String[] lines = oTin.getGeometry().split(";");
                strPoints = lines[1].replaceAll("[a-zA-Z()]", "");
                formerFeatureID = oTin.getId();
            }

            String[] points = strPoints.split(",");
            HashSet<Coordinate> pointsSet = new HashSet<Coordinate>();

            for (String line : points) {
                String[] coord = line.split(" ");
                Coordinate p = new Coordinate(Double.parseDouble(coord[0]), Double.parseDouble(coord[1]),
                        Double.parseDouble(coord[2]));
                pointsSet.add(p);
            }
            log.info("Original Points: " + pointsSet.size());

            // read lists from JSON

            // put added points from json into unique list
            List<List<Double>> newPtList = json[a].getAddedPoints();
            HashSet<Coordinate> addedPoints = new HashSet<Coordinate>();
            for (List<Double> point : newPtList) {
                Coordinate p = new Coordinate(point.get(0), point.get(1), point.get(2));
                addedPoints.add(p);
            }
            log.info("Points to add: " + addedPoints.size());

            // put removed points from json into unique list
            List<List<Double>> remPtList = json[a].getRemovedPoints();
            HashSet<Coordinate> removedPoints = new HashSet<Coordinate>();
            for (List<Double> point : remPtList) {
                Coordinate p = new Coordinate(point.get(0), point.get(1), point.get(2));
                removedPoints.add(p);
            }
            log.info("Points to remove: " + removedPoints.size());

            // put breaklines from json into line and line segments list
            List<List<List<Double>>> breaklineList = json[a].getBreaklines();
            boolean breaklines = breaklineList != null && breaklineList.size() > 0;
            LineString[] lineStringList = null;
            ArrayList<LineSegment> lineSegments = new ArrayList<LineSegment>();

            if (breaklines) {
                lineStringList = new LineString[breaklineList.size()];

                for (int f = 0; f < breaklineList.size(); f++) {
                    List<List<Double>> linePoints = breaklineList.get(f);
                    Coordinate[] lineCoordinates = new Coordinate[linePoints.size()];

                    for (int d = 0; d < linePoints.size(); d++) {
                        List<Double> coord = linePoints.get(d);
                        Coordinate p = new Coordinate(coord.get(0), coord.get(1), coord.get(2));
                        lineCoordinates[d] = p;
                    }

                    for (int i = 0; i < lineCoordinates.length - 1; i++) {
                        LineSegment segment = new LineSegment(lineCoordinates[i], lineCoordinates[i + 1]);
                        lineSegments.add(segment);
                    }

                    LineString lineString = factory.createLineString(lineCoordinates);
                    lineStringList[f] = lineString;
                }
                log.info("Added breaklines: " + lineStringList.length);
                log.info("Added breakline segments: " + lineSegments.size());

            } else
                log.info("No breaklines");
            MultiLineString lineList = factory.createMultiLineString(lineStringList);

            // update original point list with added and removed points
            pointsSet.removeAll(removedPoints);
            log.info("Points after removing: " + pointsSet.size());
            pointsSet.addAll(addedPoints);
            log.info("Points after adding: " + pointsSet.size());

            // convert point list to MultiPoint
            Coordinate[] r = new Coordinate[pointsSet.size()];
            pointsSet.toArray(r);
            MultiPoint pointList = factory.createMultiPointFromCoords(r);

            // calculate new TIN with conforming delauney triangulation
            ConformingDelaunayTriangulationBuilder confBuilder = new ConformingDelaunayTriangulationBuilder();
            confBuilder.setSites(pointList);
            confBuilder.setTolerance(0.0001);
            if (breaklines)
                confBuilder.setConstraints(lineList);
            Geometry triangles = confBuilder.getTriangles(factory);

            // filter new calculated points
            HashSet<Coordinate> pSet = new HashSet<Coordinate>(pointsSet);
            Coordinate[] tinCoords = triangles.getCoordinates();
            HashSet<Coordinate> newPts = new HashSet<Coordinate>();
            for (Coordinate t : tinCoords) {
                newPts.add(t);
            }

            for (Coordinate t : pSet) {
                newPts.remove(t);
            }
            log.info("New points after triangulation: " + newPts.size());

            // create EWKT from new TIN
            List<Polygon> polygons = new ArrayList<Polygon>();
            String tin = "SRID=";
            if (triangles instanceof GeometryCollection) {
                GeometryCollection geomColl = (GeometryCollection) triangles;
                ArrayList<Coordinate> nanPoints = new ArrayList<Coordinate>();
                ArrayList<Double> newZ = new ArrayList<Double>();

                tin += geomColl.getSRID() + ";TIN(";
                for (int i = 0; i < geomColl.getNumGeometries(); i++) {
                    Polygon poly = (Polygon) geomColl.getGeometryN(i);
                    polygons.add(poly);
                    Coordinate[] coordList = poly.getCoordinates();

                    ArrayList<Coordinate> finalList = new ArrayList<Coordinate>();

                    for (Coordinate c : coordList) {
                        if (breaklines && newPts.contains(c)) {
                            if (nanPoints.contains(c)) {
                                finalList.add(new Coordinate(c.x, c.y, newZ.get(nanPoints.indexOf(c))));
                            } else {
                                double zMinPoint = Double.NaN;
                                double d = 0.0001;
                                for (LineSegment line : lineSegments) {
                                    double dist = line.distance(c);
                                    if (dist < d) {
                                        double zValue = Vertex.interpolateZ(c, line.p0, line.p1);
                                        zMinPoint = zValue;
                                        d = dist;
                                    }
                                }
                                if (zMinPoint != Double.NaN)
                                    finalList.add(new Coordinate(c.x, c.y, zMinPoint));
                                nanPoints.add(c);
                                newZ.add(zMinPoint);
                            }
                        } else {
                            finalList.add(c);
                        }
                    }

                    tin += "((";
                    int z = 0;
                    for (Coordinate c : finalList) {
                        tin += c.x + " " + c.y + " " + c.z;
                        if (z < finalList.size() - 1)
                            tin += ",";
                        z++;
                    }
                    if (i < geomColl.getNumGeometries() - 1) {
                        tin += ")),";
                    } else {
                        tin += ")))";
                    }

                }
            }
            log.info(tin);

            WKTWriter wkt = new WKTWriter(3);
            wkt.setPrecisionModel(precisionModel);

            // push updated TIN into postgres database
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

            newTIN = tin;
            newFeatureID = updatedTin.getId();
            formerFeatureID = json[a].getObjectInfo().getFeatureId();
        }
        return result;

    }

}
