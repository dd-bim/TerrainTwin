package com.Microservices.FileInputHandler.service;

import java.io.File;
import java.io.FileOutputStream;
import java.util.UUID;

import com.Microservices.FileInputHandler.connection.GraphDBConnection;
import com.Microservices.FileInputHandler.controller.RequestController;
import com.Microservices.FileInputHandler.domain.model.PostgresInfos;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

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
    RequestController request;

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
                String export = domain + "/geometry/export/";
                String object = "postgres:" + infos.getId();
                String terrainobj = "pro:" + UUID.randomUUID().toString();
                UUID input = UUID.fromString("00000000-0000-0000-0000-000000000000");
                boolean newInput = true;
                try {
                    input = infos.getInput();
                    log.info("InputId: " + input.toString());
                } catch (Exception e) {
                    newInput = false;
                }

                // create rdf model from data
                ModelBuilder builder = new ModelBuilder();
                builder.setNamespace("postgres", namespace).setNamespace("tto", domain + "/terraintwin/ontology/")
                        .setNamespace("geo", "http://www.opengis.net/ont/geosparql#").setNamespace("pro", projNs)
                        .setNamespace("export", export);

                String postgresUrl = "export:" + infos.getUrl().replace(export, "");
                builder.add(object, RDF.TYPE, "geo:Geometry").add(terrainobj, RDF.TYPE, "geo:Feature")
                        .add(terrainobj, "geo:hasGeometry", object).add(infos.getUrl(), RDF.TYPE, "tto:GeoLink")
                        .add(object, "tto:url", postgresUrl)
                        .add(object, "geo:dimension", infos.getDimension())
                        .add(object, "geo:coordinateDimension", infos.getCoordDimension())
                        .add(object, "tto:geoVersion", infos.getVersion());

                if (infos.getPath() != null) {

                    String[] mNS = infos.getPath().split("/");
                    String minioNs = mNS[mNS.length - 1];
                    String doc = minioNs + ":" + infos.getFilename();

                    builder.setNamespace(minioNs, infos.getPath() + "/");

                    builder.add(doc, RDF.TYPE, "tto:Document")
                            .add(terrainobj, "tto:hasSource", doc);
                    if (infos.getOriginId() != -1)
                        builder.add(object, "tto:originId", infos.getOriginId());
                } else if (newInput) {

                    String processStep = "pro:" + UUID.randomUUID().toString();
                    builder.add(processStep, RDF.TYPE, "tto:TINUpdate")
                            .add(processStep, "tto:original", "postgres:" + infos.getOriginal().toString())
                            .add(processStep, "tto:input", "postgres:" + infos.getInput())
                            .add(processStep, "tto:output", object)
                            .add(processStep, "tto:editor", infos.getEditor())
                            .add(processStep, "tto:excavation", infos.getExcavation())
                            .add(processStep, "tto:backfill", infos.getBackfill())
                            .add(processStep, "tto:massChange", infos.getBackfill() - infos.getExcavation());

                    String bls = request.getBreaklines(infos.getGraphdbRepo(), infos.getInput().toString());
                    Gson gson = new Gson();
                    JsonArray blArr = gson.fromJson(bls, JsonArray.class);

                    for (JsonElement bl : blArr) {
                        builder.add("postgres:" + bl.getAsString(), "tto:breaklineOf", object);
                    }

                }

                // if geometry links another geometry, insert triple between their features
                if (infos.getLinkType() != null) {
                    String linkedGeom = "postgres:" + infos.getLinkedGeometry();
                    builder.add(object, "tto:" + infos.getLinkType(), linkedGeom);

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