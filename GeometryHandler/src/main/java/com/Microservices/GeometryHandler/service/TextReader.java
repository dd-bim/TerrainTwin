package com.Microservices.GeometryHandler.service;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import com.Microservices.GeometryHandler.connection.FileInputHandlerConnection;
import com.Microservices.GeometryHandler.domain.model.PostgresInfos;
import com.Microservices.GeometryHandler.repositories.Line2DRepository;
import com.Microservices.GeometryHandler.repositories.Line3DRepository;
import com.Microservices.GeometryHandler.repositories.Point2DRepository;
import com.Microservices.GeometryHandler.repositories.Point3DRepository;
import com.Microservices.GeometryHandler.repositories.Polygon2DRepository;
import com.Microservices.GeometryHandler.repositories.Polygon3DRepository;
import com.Microservices.GeometryHandler.repositories.SolidRepository;
import com.Microservices.GeometryHandler.repositories.TINRepository;
import com.Microservices.GeometryHandler.schemas.Line2D;
import com.Microservices.GeometryHandler.schemas.Line3D;
import com.Microservices.GeometryHandler.schemas.Point2D;
import com.Microservices.GeometryHandler.schemas.Point3D;
import com.Microservices.GeometryHandler.schemas.Polygon2D;
import com.Microservices.GeometryHandler.schemas.Polygon3D;
import com.Microservices.GeometryHandler.schemas.Solid;
import com.Microservices.GeometryHandler.schemas.TIN;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import au.com.bytecode.opencsv.CSVReader;

@Service
public class TextReader {

    @Autowired
    Point2DRepository point2DRepo;

    @Autowired
    Point3DRepository point3DRepo;

    @Autowired
    Line2DRepository line2DRepo;

    @Autowired
    Line3DRepository line3DRepo;

    @Autowired
    Polygon2DRepository polygon2DRepo;

    @Autowired
    Polygon3DRepository polygon3DRepo;

    @Autowired
    TINRepository tinRepo;

    @Autowired
    SolidRepository solidRepo;

    @Autowired
    FileInputHandlerConnection graphdb;

    @Value("${domain.url}")
    private String domain;

    Logger log = LoggerFactory.getLogger(TextReader.class);

    // Imports WKT data from CSV and writes them into a database
    public String importWKT(InputStream stream, String path, String filename, String graphdbRepo)
            throws NumberFormatException, IOException {
        log.info("Import WKT from CSV/TXT");
        CSVReader reader;
        // try { -> funktioniert nicht
        //     reader = new CSVReader(new InputStreamReader(stream), ',', '"', 0);
        // } catch (Exception e) {
            reader = new CSVReader(new InputStreamReader(stream), ';', '"', 0); // seperator sollte variabel sein
        // }

        String[] nextLine;

        // init counter
        int pointCount = 0; 
        int lineCount = 0;
        int polygonCount = 0;
        int solidCount = 0;
        int tinCount = 0;
        int notProcessed = 0;
        int idRow = -1;
        int wktRow = -1;
        int epsgRow = -1;
        String gdbConn = "";
        String urlPrefix = domain + "/geometry/export/collections/";
        


        // read the first line, where the table header should be
        // nextLine =reader.readNext()[0].split(";");
        nextLine =reader.readNext();

        // find out, in whitch column of each row are id, the geometry and possibly the epsg code
        for (int j = 0; j < nextLine.length; j++) {
            log.info(nextLine[j]);
            if (nextLine[j].toLowerCase().contains("id")){
                idRow = j;
            } else if (nextLine[j].toLowerCase().contains("wkt") || nextLine[j].toLowerCase().contains("geometry")) {
                wktRow = j;
            } else if (nextLine[j].toLowerCase().contains("epsg")){
                epsgRow = j;
            }
        }
log.info(idRow + ", " + wktRow + ", " + epsgRow);
        // read each line, check which geometry it is, create an object and save it in the database
        while ((nextLine = reader.readNext()) != null) {
            if (nextLine != null && !nextLine[idRow].isEmpty()) {
                // 3D point
                if (nextLine[wktRow].toUpperCase().replaceAll("\\s", "").contains("POINTZ")) {
                    Point3D point;
                    if (nextLine[wktRow].toUpperCase().startsWith("SRID")) {
                        point = new Point3D(Integer.parseInt(nextLine[idRow].trim()), nextLine[wktRow]);
                    } else {
                        point = new Point3D(Integer.parseInt(nextLine[idRow].trim()),
                                "SRID=" + Integer.parseInt(nextLine[epsgRow].trim()) + ";" + nextLine[wktRow]);
                    }
                    point3DRepo.save(point);
                    log.info("'ID: " + point.getId() + ", origin_id: " + point.getOrigin_id() + ", geometry: "
                            + point.getGeometry() + "'");
                    pointCount++;

                    String postgresUrl = urlPrefix + "point_3d" + "/items/" + point.getId();
                    PostgresInfos p = new PostgresInfos(point.getOrigin_id(), point.getId(), postgresUrl, 0, 3, filename, path, graphdbRepo);
                    gdbConn = graphdb.graphdbImport(p);

                    // 2D point
                } else if (nextLine[wktRow].toUpperCase().contains("POINT")) {
                    Point2D point;
                    if (nextLine[wktRow].toUpperCase().startsWith("SRID")) {
                        point = new Point2D(Integer.parseInt(nextLine[idRow].trim()), nextLine[wktRow]);
                    } else {
                        point = new Point2D(Integer.parseInt(nextLine[idRow].trim()),
                                "SRID=" + Integer.parseInt(nextLine[epsgRow].trim()) + ";" + nextLine[wktRow]);
                    }
                    point2DRepo.save(point);
                    log.info("'ID: " + point.getId() + ", origin_id: " + point.getOrigin_id() + ", geometry: "
                            + point.getGeometry() + "'");
                    pointCount++;

                    String postgresUrl = urlPrefix + "point_2d" + "/items/" + point.getId();
                    PostgresInfos p = new PostgresInfos(point.getOrigin_id(), point.getId(), postgresUrl, 0, 2, filename, path, graphdbRepo);
                    gdbConn = graphdb.graphdbImport(p);

                    // 3D line
                } else if (nextLine[wktRow].toUpperCase().replaceAll("\\s", "").contains("LINESTRINGZ")) {
                    Line3D line;
                    if (nextLine[wktRow].toUpperCase().startsWith("SRID")) {
                        line = new Line3D(Integer.parseInt(nextLine[idRow].trim()), nextLine[wktRow]);
                    } else {
                        line = new Line3D(Integer.parseInt(nextLine[idRow].trim()),
                                "SRID=" + Integer.parseInt(nextLine[epsgRow].trim()) + ";" + nextLine[wktRow]);
                    }
                    line3DRepo.save(line);
                    log.info("'ID: " + line.getId() + ", origin_id: " + line.getOrigin_id() + ", geometry: "
                            + line.getGeometry() + "'");
                    lineCount++;

                    String postgresUrl = urlPrefix + "line_3d" + "/items/" + line.getId();
                    PostgresInfos p = new PostgresInfos(line.getOrigin_id(), line.getId(), postgresUrl, 1, 3, filename, path, graphdbRepo);
                    gdbConn = graphdb.graphdbImport(p);

                    // 2D line
                } else if (nextLine[wktRow].toUpperCase().contains("LINESTRING")) {
                    Line2D line;
                    if (nextLine[wktRow].toUpperCase().startsWith("SRID")) {
                        line = new Line2D(Integer.parseInt(nextLine[idRow].trim()), nextLine[wktRow]);
                    } else {
                        line = new Line2D(Integer.parseInt(nextLine[idRow].trim()),
                                "SRID=" + Integer.parseInt(nextLine[epsgRow].trim()) + ";" + nextLine[wktRow]);
                    }
                    line2DRepo.save(line);
                    log.info("'ID: " + line.getId() + ", origin_id: " + line.getOrigin_id() + ", geometry: "
                            + line.getGeometry() + "'");
                    lineCount++;

                    String postgresUrl = urlPrefix + "line_2d" + "/items/" + line.getId();
                    PostgresInfos p = new PostgresInfos(line.getOrigin_id(), line.getId(), postgresUrl, 1, 2, filename, path, graphdbRepo);
                    gdbConn = graphdb.graphdbImport(p);

                    // 3D polygon
                } else if (nextLine[wktRow].toUpperCase().replaceAll("\\s", "").contains("POLYGONZ")) {
                    Polygon3D polygon;
                    if (nextLine[wktRow].toUpperCase().startsWith("SRID")) {
                        polygon = new Polygon3D(Integer.parseInt(nextLine[idRow].trim()), nextLine[wktRow]);
                    } else {
                        polygon = new Polygon3D(Integer.parseInt(nextLine[idRow].trim()),
                                "SRID=" + Integer.parseInt(nextLine[epsgRow].trim()) + ";" + nextLine[wktRow]);
                    }
                    polygon3DRepo.save(polygon);
                    log.info("'ID: " + polygon.getId() + ", origin_id: " + polygon.getOrigin_id() + ", geometry: "
                            + polygon.getGeometry() + "'");
                    polygonCount++;

                    String postgresUrl = urlPrefix + "polygon_3d" + "/items/" + polygon.getId();
                    PostgresInfos p = new PostgresInfos(polygon.getOrigin_id(), polygon.getId(), postgresUrl, 2, 3, filename, path, graphdbRepo);
                    gdbConn = graphdb.graphdbImport(p);

                    // 2D polygon
                } else if (nextLine[wktRow].toUpperCase().contains("POLYGON")) {
                    Polygon2D polygon;
                    if (nextLine[wktRow].toUpperCase().startsWith("SRID")) {
                        polygon = new Polygon2D(Integer.parseInt(nextLine[idRow].trim()), nextLine[wktRow]);
                    } else {
                        polygon = new Polygon2D(Integer.parseInt(nextLine[idRow].trim()),
                                "SRID=" + Integer.parseInt(nextLine[epsgRow].trim()) + ";" + nextLine[wktRow]);
                    }
                    polygon2DRepo.save(polygon);
                    log.info("'ID: " + polygon.getId() + ", origin_id: " + polygon.getOrigin_id() + ", geometry: "
                            + polygon.getGeometry() + "'");
                    polygonCount++;

                    String postgresUrl = urlPrefix + "polygon_2d" + "/items/" + polygon.getId();
                    PostgresInfos p = new PostgresInfos(polygon.getOrigin_id(), polygon.getId(), postgresUrl, 2, 2, filename, path, graphdbRepo);
                    gdbConn = graphdb.graphdbImport(p);

                    // 3D TIN
                } else if (nextLine[wktRow].toUpperCase().contains("TIN")) {

                    TIN tin;
                    if (nextLine[wktRow].toUpperCase().startsWith("SRID")) {
                        tin = new TIN(nextLine[wktRow]);
                    } else {
                        tin = new TIN("SRID=" + Integer.parseInt(nextLine[epsgRow].trim()) + ";" + nextLine[wktRow]);
                    }
                    tinRepo.save(tin);
                    log.info("'ID: " + tin.getId() + ", geometry: " + tin.getGeometry() + "'");
                    tinCount++;

                    String postgresUrl = urlPrefix + "dtm_tin" + "/items/" + tin.getId();
                    PostgresInfos p = new PostgresInfos(-1, tin.getId(), postgresUrl, 4, 3, filename, path, graphdbRepo);
                    gdbConn = graphdb.graphdbImport(p);

                    String boundary = tinRepo.getExteriorRing(tin.getId());
                    Polygon3D poly = new Polygon3D(-1, boundary);
                    polygon3DRepo.save(poly);
                    String postgresUrlBoundary = urlPrefix + "polygon_3d" + "/items/" + poly.getId();
                    PostgresInfos pBoundary = new PostgresInfos(-1, poly.getId(), postgresUrlBoundary,2, 3, filename, path, graphdbRepo, tin.getId());
                    graphdb.graphdbImport(pBoundary);

                    // solid
                } else if (nextLine[wktRow].toUpperCase().contains("POLYHEDRALSURFACE")) {
                    Solid solid;
                    if (nextLine[wktRow].toUpperCase().startsWith("SRID")) {
                        solid = new Solid(Integer.parseInt(nextLine[idRow].trim()), nextLine[wktRow]);
                    } else {
                        solid = new Solid(Integer.parseInt(nextLine[idRow].trim()),
                                "SRID=" + Integer.parseInt(nextLine[epsgRow].trim()) + ";" + nextLine[wktRow]);
                    }
                    solidRepo.save(solid);
                    log.info("'ID: " + solid.getId() + ", origin_id: " + solid.getOrigin_id() + ", geometry: "
                            + solid.getGeometry() + "'");
                    solidCount++;

                    String postgresUrl = urlPrefix + "solid" + "/items/" + solid.getId();
                    PostgresInfos p = new PostgresInfos(-1, solid.getId(), postgresUrl, 3, 3, filename, path, graphdbRepo);
                    gdbConn = graphdb.graphdbImport(p);

                } else {
                    log.error("Could not process " + nextLine[idRow] + ", " + nextLine[wktRow]);
                    notProcessed++;
                }
            }
        }
        reader.close();
        return pointCount + " Points, " + lineCount + " Lines, " + polygonCount + " Polygons, " + solidCount + " Solids and " + tinCount
                + " TINs have been imported. \n" + notProcessed + " geometries have been not imported.\n" + gdbConn;
    }

}
