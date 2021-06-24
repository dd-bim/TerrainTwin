package com.Microservices.PostgresImportService.service;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import com.Microservices.PostgresImportService.repositories.Line2DRepository;
import com.Microservices.PostgresImportService.repositories.Line3DRepository;
import com.Microservices.PostgresImportService.repositories.Point2DRepository;
import com.Microservices.PostgresImportService.repositories.Point3DRepository;
import com.Microservices.PostgresImportService.repositories.Polygon2DRepository;
import com.Microservices.PostgresImportService.repositories.Polygon3DRepository;
import com.Microservices.PostgresImportService.repositories.SolidRepository;
import com.Microservices.PostgresImportService.repositories.TINRepository;
import com.Microservices.PostgresImportService.schemas.Line2D;
import com.Microservices.PostgresImportService.schemas.Line3D;
import com.Microservices.PostgresImportService.schemas.Point2D;
import com.Microservices.PostgresImportService.schemas.Point3D;
import com.Microservices.PostgresImportService.schemas.Polygon2D;
import com.Microservices.PostgresImportService.schemas.Polygon3D;
import com.Microservices.PostgresImportService.schemas.Solid;
import com.Microservices.PostgresImportService.schemas.TIN;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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

    Logger log = LoggerFactory.getLogger(TextReader.class);

    // Imports WKT data from CSV and writes them into a database
    public String importWKT(InputStream stream, String path, String filename, String graphdbRepo)
            throws NumberFormatException, IOException {
        log.info("Import WKT from CSV/TXT");
        CSVReader reader;
        // try { -> funktioniert nicht
        //     reader = new CSVReader(new InputStreamReader(stream), ';', '"', 0);
        // } catch (Exception e) {
            reader = new CSVReader(new InputStreamReader(stream), ',', '"', 0); // seperator sollte variabel sein
        // }
        
        String[] nextLine;
        int pointCount = 0, lineCount = 0, polygonCount = 0, solidCount = 0, tinCount = 0, notProcessed = 0;
        GraphDBImport graphdb = new GraphDBImport();
        String gdbConn = "";

        int idRow = -1;
        int wktRow = -1;
        int epsgRow = -1;
        nextLine =reader.readNext();
        for (int j = 0; j < nextLine.length; j++) {
            if (nextLine[j].toLowerCase().contains("id")){
                idRow = j;
            } else if (nextLine[j].toLowerCase().contains("wkt") || nextLine[j].toLowerCase().contains("geometry")) {
                wktRow = j;
            } else if (nextLine[j].toLowerCase().contains("epsg")){
                epsgRow = j;
            }
        }
System.out.println( idRow + ", " + wktRow + ", " + epsgRow);
        while ((nextLine = reader.readNext()) != null) {
            if (nextLine != null && !nextLine[idRow].isEmpty()) {
                // 3D point
                if (nextLine[wktRow].toUpperCase().contains("POINTZ") || nextLine[wktRow].toUpperCase().contains("POINT Z")) {
                    Point3D point;
                    if (nextLine[wktRow].toUpperCase().startsWith("SRID")) {
                        point = new Point3D(Integer.parseInt(nextLine[idRow].trim()), nextLine[wktRow]);
                    } else {
                        point = new Point3D(Integer.parseInt(nextLine[idRow].trim()),
                                "SRID=" + Integer.parseInt(nextLine[epsgRow].trim()) + ";" + nextLine[wktRow]);
                    }
                    point3DRepo.save(point);
                    log.info("'ID: " + point.getId() + ", point_id: " + point.getP_id() + ", geometry: "
                            + point.getGeometry() + "'");
                    pointCount++;

                    gdbConn = graphdb.graphdbImport(point.getP_id(), point.getId(), "3DPoint", "point_3d", filename, path,
                            graphdbRepo);

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
                    log.info("'ID: " + point.getId() + ", point_id: " + point.getP_id() + ", geometry: "
                            + point.getGeometry() + "'");
                    pointCount++;

                    gdbConn = graphdb.graphdbImport(point.getP_id(), point.getId(), "2DPoint", "point_2d", filename, path,
                            graphdbRepo);

                    // 3D line
                } else if (nextLine[wktRow].toUpperCase().contains("LINESTRINGZ")
                        || nextLine[wktRow].toUpperCase().contains("LINESTRING Z")) {
                    Line3D line;
                    if (nextLine[wktRow].toUpperCase().startsWith("SRID")) {
                        line = new Line3D(Integer.parseInt(nextLine[idRow].trim()), nextLine[wktRow]);
                    } else {
                        line = new Line3D(Integer.parseInt(nextLine[idRow].trim()),
                                "SRID=" + Integer.parseInt(nextLine[epsgRow].trim()) + ";" + nextLine[wktRow]);
                    }
                    line3DRepo.save(line);
                    log.info("'ID: " + line.getId() + ", line_id: " + line.getL_id() + ", geometry: "
                            + line.getGeometry() + "'");
                    lineCount++;

                    gdbConn = graphdb.graphdbImport(line.getL_id(), line.getId(), "3DLine", "line_3d", filename, path,
                            graphdbRepo);

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
                    log.info("'ID: " + line.getId() + ", line_id: " + line.getL_id() + ", geometry: "
                            + line.getGeometry() + "'");
                    lineCount++;

                    gdbConn = graphdb.graphdbImport(line.getL_id(), line.getId(), "2DLine", "line_2d", filename, path,
                            graphdbRepo);

                    // 3D polygon
                } else if (nextLine[wktRow].toUpperCase().contains("POLYGONZ") || nextLine[wktRow].toUpperCase().contains("POLYGON Z")) {
                    Polygon3D polygon;
                    if (nextLine[wktRow].toUpperCase().startsWith("SRID")) {
                        polygon = new Polygon3D(Integer.parseInt(nextLine[idRow].trim()), nextLine[wktRow]);
                    } else {
                        polygon = new Polygon3D(Integer.parseInt(nextLine[idRow].trim()),
                                "SRID=" + Integer.parseInt(nextLine[epsgRow].trim()) + ";" + nextLine[wktRow]);
                    }
                    polygon3DRepo.save(polygon);
                    log.info("'ID: " + polygon.getId() + ", polygon_id: " + polygon.getSurfaceID() + ", geometry: "
                            + polygon.getGeometry() + "'");
                    polygonCount++;

                    gdbConn = graphdb.graphdbImport(polygon.getSurfaceID(), polygon.getId(), "3DPolygon", "polygon_3d", filename,
                            path, graphdbRepo);

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
                    log.info("'ID: " + polygon.getId() + ", polygon_id: " + polygon.getSurfaceID() + ", geometry: "
                            + polygon.getGeometry() + "'");
                    polygonCount++;

                    gdbConn = graphdb.graphdbImport(polygon.getSurfaceID(), polygon.getId(), "2DPolygon", "polygon_2d", filename,
                            path, graphdbRepo);

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

                    gdbConn = graphdb.graphdbImport(-1, tin.getId(), "TIN", "dtm_tin", filename, path,
                            graphdbRepo);

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
                    log.info("'ID: " + solid.getId() + ", solid_id: " + solid.getS_id() + ", geometry: "
                            + solid.getGeometry() + "'");
                    solidCount++;

                    gdbConn = graphdb.graphdbImport(solid.getS_id(), solid.getId(), "Solid", "solid", filename,
                            path, graphdbRepo);

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
