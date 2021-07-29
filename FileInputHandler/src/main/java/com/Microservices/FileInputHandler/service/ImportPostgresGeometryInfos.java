package com.Microservices.FileInputHandler.service;

import java.io.File;
import java.io.FileOutputStream;
import java.util.UUID;

import com.Microservices.FileInputHandler.connection.GraphDBConnection;
import com.Microservices.FileInputHandler.connection.MinIOConnection;
import com.Microservices.FileInputHandler.domain.model.PostgresInfos;

import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.model.Resource;
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
                String[] mNS= infos.getPath().split("/");
                String minioNs = mNS[mNS.length - 1];

                // create rdf model from data
                ModelBuilder builder = new ModelBuilder();
                builder.setNamespace("postgres", namespace)
                        .setNamespace("tto", domain + "/terraintwin/ontology/")
                        .setNamespace("geo", "http://www.opengis.net/ont/geosparql#")
                        .setNamespace("pro", domain + "/project/")
                        .setNamespace(minioNs, infos.getPath() + "/")
                        .setNamespace("export", domain + "/geometry/export/");
                String object = "postgres:" + infos.getId();
                String terrainobj = "pro:" + UUID.randomUUID().toString();
                String doc = minioNs + ":" + infos.getFilename();
                builder.add(object, "rdf:type", "geo:Geometry").add(terrainobj, "rdf:type", "geo:Feature")
                        .add(doc, "rdf:type", "tto:Document").add(terrainobj, "geo:hasGeometry", object)
                        .add(terrainobj, "tto:hasSource", doc).add(object, "tto:originId", infos.getOriginId())
                        // .add(object, "tto:url", infos.getUrl())
                        .add(infos.getUrl(), "rdf:type", "tto:GeoLink")
                        .add(object, "tto:url", "export:" + infos.getUrl().replace(domain + "/geometry/export/", ""))
                        .add(object, "geo:dimension", infos.getDimension())
                        .add(object, "geo:coordinateDimension", infos.getCoordDimension());

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