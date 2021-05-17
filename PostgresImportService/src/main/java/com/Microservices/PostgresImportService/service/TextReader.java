package com.Microservices.PostgresImportService.service;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import com.Microservices.PostgresImportService.repositories.LineRepository;
import com.Microservices.PostgresImportService.repositories.PointRepository;
import com.Microservices.PostgresImportService.repositories.PolygonRepository;
import com.Microservices.PostgresImportService.schemas.Line;
import com.Microservices.PostgresImportService.schemas.Point;
import com.Microservices.PostgresImportService.schemas.Polygon;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import au.com.bytecode.opencsv.CSVReader;

@Service
public class TextReader {

    @Autowired
    PolygonRepository polygonRepo;

    @Autowired
    PointRepository pointRepo;

    @Autowired
    LineRepository lineRepo;

    Logger log = LoggerFactory.getLogger(TextReader.class);

    // Imports WKT data from CSV and writes them into a database
    public String importWKT(InputStream stream) throws NumberFormatException, IOException {
        log.info("Import WKT from CSV/TXT");
        CSVReader reader = new CSVReader(new InputStreamReader(stream), ',', '"', 1); // seperator sollte variabel sein
        String[] nextLine;
        int pointCount = 0, lineCount = 0, polygonCount = 0;

        while ((nextLine = reader.readNext()) != null) {
            if (nextLine != null) {
                if (nextLine[1].toUpperCase().contains("POINT")) {

                    Point point = new Point(Integer.parseInt(nextLine[0].trim()),
                            "SRID=" + Integer.parseInt(nextLine[2].trim()) + ";" + nextLine[1]);
                    pointRepo.save(point);
                    log.info("'ID: " + point.getId() + ", point_id: " + point.getP_id() + ", geometry: "
                            + point.getGeometry() + "'");
                    pointCount++;

                } else if (nextLine[1].toUpperCase().contains("LINESTRING")) {

                    Line line = new Line(Integer.parseInt(nextLine[0].trim()),
                            "SRID=" + Integer.parseInt(nextLine[2].trim()) + ";" + nextLine[1]);
                    lineRepo.save(line);
                    log.info("'ID: " + line.getId() + ", line_id: " + line.getL_id() + ", geometry: "
                            + line.getGeometry() + "'");
                    lineCount++;

                } else if (nextLine[1].toUpperCase().contains("POLYGON")) {

                    Polygon polygon = new Polygon(Integer.parseInt(nextLine[0].trim()),
                            "SRID=" + Integer.parseInt(nextLine[2].trim()) + ";" + nextLine[1]);
                    polygonRepo.save(polygon);
                    log.info("'ID: " + polygon.getId() + ", polygon_id: " + polygon.getSurfaceID() + ", geometry: "
                            + polygon.getGeometry() + "'");
                    polygonCount++;
                }

            }
        }
        reader.close();
        return pointCount + " Points, " + lineCount + " Lines, " + polygonCount + " Polygons have been imported.";
    }
}
