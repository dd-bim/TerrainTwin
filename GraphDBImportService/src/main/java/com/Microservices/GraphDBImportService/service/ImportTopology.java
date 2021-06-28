package com.Microservices.GraphDBImportService.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;

import com.Microservices.GraphDBImportService.connection.GraphDBConnection;
import com.Microservices.GraphDBImportService.connection.MinIOConnection;
import com.Microservices.GraphDBImportService.domain.model.PostgresInfos;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.commons.io.FilenameUtils;
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

import io.minio.GetObjectArgs;
import io.minio.ListObjectsArgs;
import io.minio.MinioClient;
import io.minio.Result;
import io.minio.errors.MinioException;
import io.minio.messages.Item;

@Service
public class ImportTopology {

    @Autowired
    MinIOConnection connection;

    @Autowired
    GraphDBConnection dbconnection;

    Logger log = LoggerFactory.getLogger(ImportTopology.class);

    @Value("${minio.url}")
    private String url;
    @Value("${domain.url}")
    private String domain;

    public String importTopo(String json, String repo) throws Exception {

        String results = "";
        // String filename = "";

        // get connection to graphdb repository
        try {
            RepositoryConnection db = dbconnection.connection(repo);

            String namespace = domain + "/postgres/";

            ModelBuilder builder = new ModelBuilder();
            builder.setNamespace("postgres", namespace);
            // json.forEach((k, v) -> {
            // if (k.equals("type")) {
            // builder.add(resource, RDF.TYPE, "meta:" + v);
            // } else {
            // builder.add(resource, "meta:" + k, v);
            // }
            // });
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
            // results += ("Metadaten zur Datei " + resource + " importiert.") + "\n";

    }catch(

    Exception e)
    {
        results += "Could not connect to GraphDB. Possibly the database is not available. \n Message: "
                + e.getMessage();
    }return results;
    }

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
                builder.setNamespace("postgres", namespace).setNamespace("geom", "http://geometry.example.org/");
                String object = "postgres:" + infos.getOriginId();
                builder.add(object, "geom:source", infos.getPath() + "/" + infos.getFilename())
                        .add(object, "geom:id", infos.getId()).add(object, "geom:url", infos.getUrl())
                        .add(object, "geom:Type", infos.getType());

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