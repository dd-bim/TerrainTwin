package com.Microservices.PostgresImportService.service;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import com.Microservices.PostgresImportService.repositories.Line2DRepository;
import com.Microservices.PostgresImportService.repositories.Line3DRepository;
import com.Microservices.PostgresImportService.repositories.Point2DRepository;
import com.Microservices.PostgresImportService.repositories.Point3DRepository;
import com.Microservices.PostgresImportService.repositories.Polygon2DRepository;
import com.Microservices.PostgresImportService.repositories.TINRepository;
import com.Microservices.PostgresImportService.schemas.Line2D;
import com.Microservices.PostgresImportService.schemas.Line3D;
import com.Microservices.PostgresImportService.schemas.Point2D;
import com.Microservices.PostgresImportService.schemas.Point3D;
import com.Microservices.PostgresImportService.schemas.Polygon2D;
import com.Microservices.PostgresImportService.schemas.TIN;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import au.com.bytecode.opencsv.CSVReader;

@Service
public class TextReader {

    @Autowired
    Polygon2DRepository polygon2DRepo;

    @Autowired
    Point2DRepository point2DRepo;

    @Autowired
    Point3DRepository point3DRepo;

    @Autowired
    Line2DRepository line2DRepo;

    @Autowired
    Line3DRepository line3DRepo;

    @Autowired
    TINRepository tinRepo;

    Logger log = LoggerFactory.getLogger(TextReader.class);

    // Imports WKT data from CSV and writes them into a database
    public String importWKT(InputStream stream) throws NumberFormatException, IOException {
        log.info("Import WKT from CSV/TXT");
        CSVReader reader = new CSVReader(new InputStreamReader(stream), ',', '"', 1); // seperator sollte variabel sein
        String[] nextLine;
        int pointCount = 0, lineCount = 0, polygonCount = 0, tinCount = 0, notProcessed = 0;

        while ((nextLine = reader.readNext()) != null) {
            if (nextLine != null && !nextLine[0].isEmpty()) {
                // 3D point
                if (nextLine[1].toUpperCase().contains("POINTZ") || nextLine[1].toUpperCase().contains("POINT Z")) {
                    Point3D point;
                    if (nextLine[1].toUpperCase().startsWith("SRID")) {
                        point = new Point3D(Integer.parseInt(nextLine[0].trim()), nextLine[1]);
                    } else {
                        point = new Point3D(Integer.parseInt(nextLine[0].trim()),
                                "SRID=" + Integer.parseInt(nextLine[2].trim()) + ";" + nextLine[1]);
                    }
                    point3DRepo.save(point);
                    log.info("'ID: " + point.getId() + ", point_id: " + point.getP_id() + ", geometry: "
                            + point.getGeometry() + "'");
                    pointCount++;

                    // 2D point
                } else if (nextLine[1].toUpperCase().contains("POINT")) {
                    Point2D point;
                    if (nextLine[1].toUpperCase().startsWith("SRID")) {
                        point = new Point2D(Integer.parseInt(nextLine[0].trim()), nextLine[1]);
                    } else {
                        point = new Point2D(Integer.parseInt(nextLine[0].trim()),
                                "SRID=" + Integer.parseInt(nextLine[2].trim()) + ";" + nextLine[1]);
                    }
                    point2DRepo.save(point);
                    log.info("'ID: " + point.getId() + ", point_id: " + point.getP_id() + ", geometry: "
                            + point.getGeometry() + "'");
                    pointCount++;

                    // 3D line
                } else if (nextLine[1].toUpperCase().contains("LINESTRINGZ")
                        || nextLine[1].toUpperCase().contains("LINESTRING Z")) {
                    Line3D line;
                    if (nextLine[1].toUpperCase().startsWith("SRID")) {
                        line = new Line3D(Integer.parseInt(nextLine[0].trim()), nextLine[1]);
                    } else {
                        line = new Line3D(Integer.parseInt(nextLine[0].trim()),
                                "SRID=" + Integer.parseInt(nextLine[2].trim()) + ";" + nextLine[1]);
                    }
                    line3DRepo.save(line);
                    log.info("'ID: " + line.getId() + ", line_id: " + line.getL_id() + ", geometry: "
                            + line.getGeometry() + "'");
                    lineCount++;

                    // 2D line
                } else if (nextLine[1].toUpperCase().contains("LINESTRING")) {
                    Line2D line;
                    if (nextLine[1].toUpperCase().startsWith("SRID")) {
                        line = new Line2D(Integer.parseInt(nextLine[0].trim()), nextLine[1]);
                    } else {
                        line = new Line2D(Integer.parseInt(nextLine[0].trim()),
                                "SRID=" + Integer.parseInt(nextLine[2].trim()) + ";" + nextLine[1]);
                    }
                    line2DRepo.save(line);
                    log.info("'ID: " + line.getId() + ", line_id: " + line.getL_id() + ", geometry: "
                            + line.getGeometry() + "'");
                    lineCount++;

                    // 2D polygon
                } else if (nextLine[1].toUpperCase().contains("POLYGON")) {
                    Polygon2D polygon;
                    if (nextLine[1].toUpperCase().startsWith("SRID")) {
                        polygon = new Polygon2D(Integer.parseInt(nextLine[0].trim()), nextLine[1]);
                    } else {
                        polygon = new Polygon2D(Integer.parseInt(nextLine[0].trim()),
                                "SRID=" + Integer.parseInt(nextLine[2].trim()) + ";" + nextLine[1]);
                    }
                    polygon2DRepo.save(polygon);
                    log.info("'ID: " + polygon.getId() + ", polygon_id: " + polygon.getSurfaceID() + ", geometry: "
                            + polygon.getGeometry() + "'");
                    polygonCount++;

                    // 3D TIN
                } else if (nextLine[1].toUpperCase().contains("TIN")) {

                    TIN tin;
                    if (nextLine[1].toUpperCase().startsWith("SRID")) {
                        tin = new TIN(nextLine[1]);
                    } else {
                        tin = new TIN("SRID=" + Integer.parseInt(nextLine[2].trim()) + ";" + nextLine[1]);
                    }
                    tinRepo.save(tin);
                    log.info("'ID: " + tin.getTin_id() + ", geometry: " + tin.getGeometry() + "'");
                    tinCount++;
                } else {
                    log.error("Could not process " +nextLine[0] + ", " + nextLine[1]);
                    notProcessed++;
                }
            }
        }
        reader.close();
        return pointCount + " Points, " + lineCount + " Lines, " + polygonCount + " Polygons and " + tinCount
                + " TINs have been imported. \n" + notProcessed + " geometries have been not imported.";
    }
}
