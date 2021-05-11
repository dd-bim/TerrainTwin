package com.Microservices.PostgresImportService.service;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import com.Microservices.PostgresImportService.repositories.PolygonRepository;
import com.Microservices.PostgresImportService.schemas.Polygon;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.com.bytecode.opencsv.CSVReader;

public class ImportWKT {  

    Logger log = LoggerFactory.getLogger(ImportWKT.class);
    
    // Imports WKT data from CSV and writes them into a database
    public String importWKT(InputStream stream, PolygonRepository repository) throws NumberFormatException, IOException {
        log.info("Import Polygons");
        String results = "Polygons have been imported. ";
        CSVReader reader = new CSVReader(new InputStreamReader(stream), ',', '"', 1);
        String[] nextLine;

            while ((nextLine = reader.readNext()) != null) {
                if (nextLine != null) {
                    // Surfaces surface = new Surfaces(Integer.parseInt(nextLine[0].trim()), nextLine[1], Integer.parseInt(nextLine[2].trim()));
                    Polygon polygon = new Polygon(Integer.parseInt(nextLine[0].trim()), "SRID="+ Integer.parseInt(nextLine[2].trim())+ ";" + nextLine[1]);
                    repository.save(polygon);
                    log.info("'ID: "+polygon.getId()+", polygon_id: "+polygon.getSurfaceID()+", geometry: "+polygon.getGeometry()+"'");
                }
            }
        reader.close();
       return results;
    }
}
