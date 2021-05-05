package com.Microservices.PostgresImportService.service;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import com.Microservices.PostgresImportService.repositories.SurfaceRepository;
import com.Microservices.PostgresImportService.schemas.Surfaces;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.com.bytecode.opencsv.CSVReader;

public class ImportWKT {  

    Logger log = LoggerFactory.getLogger(ImportWKT.class);
    
    // Imports WKT data from CSV and writes them into a database
    public String importWKT(InputStream stream, SurfaceRepository repository) throws NumberFormatException, IOException {
        log.info("Import Surfaces");
        String results = "Surfaces have been imported. ";
        CSVReader reader = new CSVReader(new InputStreamReader(stream), ',', '"', 1);
        String[] nextLine;

            while ((nextLine = reader.readNext()) != null) {
                if (nextLine != null) {
                    Surfaces surface = new Surfaces(Integer.parseInt(nextLine[0].trim()), nextLine[1], Integer.parseInt(nextLine[2].trim()));
                    repository.save(surface);
                    log.info("'ID: "+surface.getId()+", surface_id: "+surface.getSurfaceID()+", WKT: "+surface.getGeometry()+", SRID: "+surface.getSrid()+"'");
                }
            }
        reader.close();
       return results;
    }
}
