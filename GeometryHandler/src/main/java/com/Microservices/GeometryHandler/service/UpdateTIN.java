package com.Microservices.GeometryHandler.service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.Microservices.GeometryHandler.connection.FileInputHandlerConnection;
import com.Microservices.GeometryHandler.controller.ExportController;
import com.Microservices.GeometryHandler.domain.model.IndexedTin;
import com.Microservices.GeometryHandler.domain.model.PostgresInfos;
import com.Microservices.GeometryHandler.domain.model.UpdateJSON;
import com.Microservices.GeometryHandler.domain.model.Volume;
import com.Microservices.GeometryHandler.repositories.BreaklinesRepository;
import com.Microservices.GeometryHandler.repositories.Polygon3DRepository;
import com.Microservices.GeometryHandler.repositories.TINRepository;
import com.Microservices.GeometryHandler.schemas.Breaklines;
import com.Microservices.GeometryHandler.schemas.Polygon3D;
import com.Microservices.GeometryHandler.schemas.TIN;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.CoordinateList;
import org.locationtech.jts.geom.CoordinateSequence;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryCollection;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LineSegment;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.LinearRing;
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
    VolumeCalculation getVolume;

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
        JsonArray resultArray = new JsonArray();

        // deserialize json string to POJO
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        UpdateJSON[] json = null;
        try {
            json = gson.fromJson(input, UpdateJSON[].class);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }

        GeometryCollection oldTin = new GeometryCollection(new Geometry[0], new GeometryFactory());
        UUID formerFeatureID = null;
        UUID newFeatureID = null;

        // start loop over TIN updates in JSON
        for (int a = 0; a < json.length; a++) {
            PrecisionModel precisionModel = new PrecisionModel(1000);
            GeometryFactory factory = new GeometryFactory(precisionModel, 25832);
            WKTReader wkt = new WKTReader(factory);
            JsonObject resultJson = new JsonObject();

            // check, if same TIN as in former loop is used
            // put start TIN to point list
            CoordinateList pointList = new CoordinateList();
            if (json[a].getObjectInfo().getFeatureId().equals(formerFeatureID)) {
                pointList = new CoordinateList(oldTin.getCoordinates());
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

                oldTin = (GeometryCollection) wkt.read(geometry);
                pointList = new CoordinateList(oldTin.getCoordinates());
                newFeatureID = dbTin.getId();

            }
            System.out.println("Starting points: " + pointList.size());

            // read lists from JSON

            // put added points from json into unique list
            CoordinateList addedPoints = helper.getPointsFromList(json[a].getAddedPoints());
            System.out.println("Points to add: " + addedPoints.size());

            // put removed points from json into unique list
            CoordinateList removedPoints = helper.getPointsFromList(json[a].getRemovedPoints());
            System.out.println("Points to remove: " + removedPoints.size());

            // create updated point list
            pointList.removeAll(removedPoints);
            System.out.println("Points after removing: " + pointList.size());
            pointList.addAll(addedPoints);
            System.out.println("Points after adding: " + pointList.size());

            // make a delaunay triangulation from point list
            GeometryCollection dtTriangles = helper.triangulateDT(pointList, factory);
            CoordinateList dtPoints = new CoordinateList(dtTriangles.getCoordinates());

            // create a indexed tin from created
            IndexedTin dtTinIndexed = TinTriangleList.createIndexedTriangles(dtTriangles);
            Tin dtTin = Tin.CreateTin(dtTinIndexed.Points, dtTinIndexed.Triangles, 0);

            // put breaklines from json into line and line segments list
            List<List<List<Double>>> breaklineList = json[a].getBreaklines();
            boolean breaklines = breaklineList != null && breaklineList.size() > 0;
            LineString[] lineStringList = null;
            ArrayList<LineSegment> lineSegments = new ArrayList<LineSegment>();

            if (breaklines) {
                lineStringList = new LineString[breaklineList.size()];

                for (int f = 0; f < breaklineList.size(); f++) {
                    List<List<Double>> list = breaklineList.get(f);
                    CoordinateList linePointList = new CoordinateList();
                    for (List<Double> point : list) {
                        Coordinate p = new Coordinate();
                        if (point.size() == 3) {
                            p = new Coordinate(point.get(0), point.get(1), point.get(2));
                        // if breaklines are 2d, interpolate z values on created tin
                        } else if (point.size() == 2) {
                            double z  = dtTin.GetTriangle(new double[] {point.get(0), point.get(1)}).DoubleValue;
                            p = new Coordinate(point.get(0), point.get(1), z);
                        }
                        linePointList.add(p);
                    }

                    for (int i = 0; i < linePointList.size() - 1; i++) {
                        lineSegments.add(new LineSegment(linePointList.get(i), linePointList.get(i + 1)));
                    }

                    lineStringList[f] = factory.createLineString(linePointList.toCoordinateArray());
                }
                System.out.println("Added breaklines: " + lineStringList.length);
                System.out.println("Added breakline segments: " + lineSegments.size());

            } else {
                System.out.println("No breaklines");
            }
            MultiLineString lineList = factory.createMultiLineString(lineStringList);

            // calculate new TIN with conforming delauney triangulation
            GeometryCollection triangles = helper.triangulate(dtPoints, lineList, factory);

            // cut new TIN by boundary of original to restore concave forms
            CoordinateSequence cs = new CoordinateArraySequence(oldTin.union().getBoundary().getCoordinates());
            triangles = helper.cutCollectionByBoundary(triangles, new LinearRing(cs, factory), factory);

            // filter new calculated points
            CoordinateList newPointList = new CoordinateList(triangles.getCoordinates());
            newPointList.removeAll(pointList);
            System.out.println("New points after triangulation: " + newPointList.size());

            // interpolate all z values of new points and create polygon list
            Polygon[] polygonList = helper.interpolateNaNValues(triangles, lineSegments, newPointList, breaklines,
                    factory);

            // write polygon list to WKT
            GeometryCollection newTin = factory.createGeometryCollection(polygonList);
            WKTWriter writer = new WKTWriter(3);
            writer.setPrecisionModel(precisionModel);
            String gcWkt = writer.write(newTin);

            // push updated TIN into postgres database
            String tin = "SRID=" + newTin.getSRID() + ";"
                    + gcWkt.replace("GEOMETRYCOLLECTION Z", "TIN").replaceAll("POLYGON Z", "");
            log.info(tin);
            TIN updatedTin = new TIN(tin);
            tinRepository.save(updatedTin);
            log.info("'ID: " + updatedTin.getId() + ", WKT: " + updatedTin.getGeometry() + "'");
            resultJson.addProperty("updated TIN", updatedTin.getId().toString());

            // calculate volume between old and new TIN
            Volume volume = getVolume.calculateVolume(oldTin, newTin, null, json[a].getObjectInfo().getSrid());
            resultJson.addProperty("Excavation [cbm]", volume.Negative);
            resultJson.addProperty("Backfill [cbm]", volume.Positive);

            // get version and optional original tin from input tin
            String res = graphdb.graphdbGetTINInfos(newFeatureID.toString(), repo);
            JsonObject resJson = gson.fromJson(res, JsonObject.class);

            String newVersion = "1.0";
            UUID original = newFeatureID;
            try {
                char[] v = resJson.get("version").toString().replaceAll("\"", "").toCharArray();

                for (int i = 0; i < v.length; i++) {
                    if (Character.isDigit(v[i])) {
                        int d = Character.getNumericValue(v[i]) + 1;
                        v[i] = Character.forDigit(d, 10);
                        newVersion = new String(v);
                        break;
                    }
                }

                // if the original is empty, input is also the original tin
                try {
                    String[] s = resJson.get("original").toString().replaceAll("\"", "").split("\\/");
                    original = UUID.fromString(s[s.length - 1]);
                } catch (Exception e) {
                    log.error("No original found");
                }

            } catch (Exception e) {
                log.error("No tin for id " + newFeatureID + " found.");
            }

            // send update information to GraphDB
            String postgresUrl = urlPrefix + "dtm_tin" + "/items/" + updatedTin.getId();
            PostgresInfos p = new PostgresInfos(updatedTin.getId(), newFeatureID, original, newVersion,
                    json[a].getMetaInfos().getUser(), postgresUrl, 4, 3, volume.Negative, volume.Positive, repo);
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
                    String l = writer.write(lineStringList[i]);
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

            resultArray.add(resultJson);

            oldTin = newTin;
            newFeatureID = updatedTin.getId();
            formerFeatureID = json[a].getObjectInfo().getFeatureId();
        }
    
    return gson.toJson(resultArray);
    }
}
