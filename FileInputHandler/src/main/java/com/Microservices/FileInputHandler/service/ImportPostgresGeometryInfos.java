package com.Microservices.FileInputHandler.service;

import java.io.File;
import java.io.FileOutputStream;
import java.util.UUID;

import com.Microservices.FileInputHandler.connection.GraphDBConnection;
import com.Microservices.FileInputHandler.domain.model.PostgresInfos;
import com.Microservices.FileInputHandler.domain.model.Queries;

import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.model.util.ModelBuilder;
import org.eclipse.rdf4j.model.vocabulary.RDF;
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
    GraphDBConnection dbconnection;

    @Autowired
    QueryExecution exec;

    @Autowired
    Queries query;

    @Value("${domain.url}")
    private String domain;

    Logger log = LoggerFactory.getLogger(ImportPostgresGeometryInfos.class);

    // import postgres infos from geometries
    public String importPostgresInfos(PostgresInfos infos) {

        String results = "";

        // get connection to graphdb repository
        try {
            RepositoryConnection db = dbconnection.connection(infos.getGraphdbRepo());

            if (db != null) {

                String namespace = domain + "/postgres/";
                String projNs = domain + "/project/";
                String[] mNS = infos.getPath().split("/");
                String minioNs = mNS[mNS.length - 1];

                // create rdf model from data
                ModelBuilder builder = new ModelBuilder();
                builder.setNamespace("postgres", namespace).setNamespace("tto", domain + "/terraintwin/ontology/")
                        .setNamespace("geo", "http://www.opengis.net/ont/geosparql#").setNamespace("pro", projNs)
                        .setNamespace(minioNs, infos.getPath() + "/")
                        .setNamespace("export", domain + "/geometry/export/");
                String object = "postgres:" + infos.getId();
                String terrainobj = "pro:" + UUID.randomUUID().toString();
                String doc = minioNs + ":" + infos.getFilename();
                builder.add(object, RDF.TYPE, "geo:Geometry").add(terrainobj, RDF.TYPE, "geo:Feature")
                        .add(doc, RDF.TYPE, "tto:Document").add(terrainobj, "geo:hasGeometry", object)
                        .add(terrainobj, "tto:hasSource", doc).add(object, "tto:originId", infos.getOriginId())
                        .add(infos.getUrl(), RDF.TYPE, "tto:GeoLink")
                        .add(object, "tto:url", "export:" + infos.getUrl().replace(domain + "/geometry/export/", ""))
                        .add(object, "geo:dimension", infos.getDimension())
                        .add(object, "geo:coordinateDimension", infos.getCoordDimension());

                // if geometry bounds another geometry, insert triple between their features
                UUID nil = UUID.fromString("00000000-0000-0000-0000-000000000000");
                if (!infos.getBounds().equals(nil)) {
                    String feature = exec.executeQuery(infos.getGraphdbRepo(),
                            query.findFeature(namespace + infos.getBounds().toString()));

                    // add "bounds" triple
                    builder.add(terrainobj, "tto:bounds", "pro:" + feature.replace(projNs, ""));

                }

                // if TIN add type Coverage
                if (infos.getDimension().equals(4)) {
                    builder.add(terrainobj, RDF.TYPE, "tto:Coverage");

                // if polygon add type TopograficElement
                } else if (infos.getDimension().equals(2)) {
                    builder.add(terrainobj, RDF.TYPE, "tto:TopograficElement");

                // else actually add type TopograficElement
                } else {
                    builder.add(terrainobj, RDF.TYPE, "tto:TopograficElement");
                }

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