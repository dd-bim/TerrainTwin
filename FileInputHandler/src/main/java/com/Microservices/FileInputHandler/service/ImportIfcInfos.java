package com.Microservices.FileInputHandler.service;

import java.io.File;
import java.io.FileOutputStream;
import java.util.UUID;

import com.Microservices.FileInputHandler.connection.GraphDBConnection;

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
public class ImportIfcInfos {

    @Autowired
    GraphDBConnection dbconnection;

    Logger log = LoggerFactory.getLogger(ImportIfcInfos.class);

    @Value("${domain.url}")
    private String domain;

    public String importIfcInfos(Long poid, Long roid, UUID uuid, String filename, String bucket, String repo) {

        String results = "";

        // get connection to graphdb repository
        try {
            RepositoryConnection db = dbconnection.connection(repo);

            if (db != null) {

                String namespace = domain + "/bimserver/";
                String minioNs = domain + "/minio/" + bucket + "/";

                // create rdf model from data
                ModelBuilder builder = new ModelBuilder();
                builder.setNamespace("bim", namespace).setNamespace("tto", domain + "/terraintwin/ontology/")
                        .setNamespace("pro", domain + "/project/").setNamespace(bucket, minioNs );
                
                String object = "bim:" + uuid;
                String doc = bucket + ":" + filename;
                builder.add(object, RDF.TYPE, "tto:BuildingElement")
                        .add(doc, RDF.TYPE, "tto:Document")
                        .add(object, "tto:hasSource", doc)
                        .add(object, "tto:ifcProjectId", poid)
                        .add(object, "tto:ifcRevisionId", roid);

                Model m = builder.build();

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