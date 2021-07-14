package com.Microservices.FileInputHandler.service;

import java.io.File;
import java.io.FileOutputStream;

import com.Microservices.FileInputHandler.connection.GraphDBConnection;
import com.Microservices.FileInputHandler.connection.MinIOConnection;
import com.Microservices.FileInputHandler.domain.model.PostgresInfos;

import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.model.util.ModelBuilder;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.eclipse.rdf4j.rio.RDFFormat;
import org.eclipse.rdf4j.rio.Rio;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class ImportPostgresGeometryInfos {

    @Autowired
    MinIOConnection connection;

    @Autowired
    GraphDBConnection dbconnection;

    Logger log = LoggerFactory.getLogger(ImportPostgresGeometryInfos.class);

    @Value("${minio.url}")
    private String url;
    @Value("${domain.url}")
    private String domain;

    // import postgres infos from geometries
    public String importPostgresInfos(PostgresInfos infos) {

        String results = "";

        // get connection to graphdb repository
        try {
            RepositoryConnection db = dbconnection.connection(infos.getGraphdbRepo());

            if (db != null) {

                String namespace = domain + "/postgres/";

                // create rdf model from data
                ModelBuilder builder = new ModelBuilder();
                builder.setNamespace("postgres", namespace).setNamespace("tto", "http://terrain.dd-bim.org/terraintwin/ontology/");
                String object = "postgres:" + infos.getId();
                builder.add(object, "tto:source", infos.getPath() + "/" + infos.getFilename())
                        .add(object, "tto:origin_id", infos.getOriginId()).add(object, "tto:url", infos.getUrl())
                        .add(object, "geo:dimension", infos.getDimension()).add(object, "geo:coordinateDimension", infos.getCoordDimension());

                Model m = builder.build();
                log.info(m.toString());

                // write model in turtle file and import into repository
                File tmp = File.createTempFile("turtle", "tmp");
                FileOutputStream out = new FileOutputStream(tmp);
                try {
                    Rio.write(m, out, RDFFormat.TURTLE);
                } finally {
                    out.close();
                }
                db.add(tmp, namespace, RDFFormat.TURTLE);

                results += "Imported all data.";
                log.info(results);
            } else {
                results += "Repository didn't exist.";
                System.out.println("Connection to repository failed.");
            }

        } catch (Exception e) {
            results += "Could not connect to GraphDB. Possibly the database is not available. \n Message: "
                    + e.getMessage();
        }
        return results;
    }

}