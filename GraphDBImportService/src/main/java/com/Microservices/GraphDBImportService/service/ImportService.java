package com.Microservices.GraphDBImportService.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
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
public class ImportService {

    @Autowired
    MinIOConnection connection;

    @Autowired
    GraphDBConnection dbconnection;

    Logger log = LoggerFactory.getLogger(ImportService.class);

    @Value("${minio.url}")
    private String url;
    @Value("${domain.url}")
    private String domain;

    // get files of spezified bucket and import into spezified graphdb repository
    public String getFiles(String bucket, String repo) throws Exception {

        String results = "";
        String filename = "";

        // get connection to graphdb repository
        try {
            RepositoryConnection db = dbconnection.connection(repo);

            // Lists objects information
            try {
                // get connection to Minio
                MinioClient client = connection.connection();

                Iterable<Result<Item>> objects = client.listObjects(ListObjectsArgs.builder().bucket(bucket).build());
                for (Result<Item> result : objects) {
                    filename = result.get().objectName();
                    String extension = FilenameUtils.getExtension(filename);

                    if (extension.equals("ttl") || extension.equals("rdf") || extension.equals("owl")) {

                        // get stream from given bucket and object name
                        try (InputStream stream = client
                                .getObject(GetObjectArgs.builder().bucket(bucket).object(filename).build())) {

                            String baseURI = url + "/" + bucket + "/" + filename + "/";

                            // import turtle file into repository
                            if (extension.equals("ttl")) {
                                db.add(stream, baseURI, RDFFormat.TURTLE);
                            }

                            // import rdf/xml or owl file into repository
                            if (extension.equals("rdf") || extension.equals("owl")) {
                                db.add(stream, baseURI, RDFFormat.RDFXML);
                            }

                            results += (filename + " importiert.") + "\n";

                        } catch (IOException e) {
                            log.error(e.getMessage());
                            results += e.getMessage() + "\n";
                        }
                    }

                    // check if file is metadata file
                    if (extension.equals("json") && filename.contains("metadata")) {

                        // get stream from given bucket and object name
                        try (InputStream stream = client
                                .getObject(GetObjectArgs.builder().bucket(bucket).object(filename).build())) {

                            // get key-value-paires from json
                            HashMap<String, Object> json = new HashMap<String, Object>();
                            @SuppressWarnings("unchecked")
                            HashMap<String, Object> obj = new ObjectMapper().readValue(stream, HashMap.class);
                            obj.forEach((k, v) -> {
                                if (v.getClass() == String.class) {
                                    json.put(k, v);
                                } else {
                                    @SuppressWarnings("unchecked")
                                    HashMap<String, Object> s = (HashMap<String, Object>) v;
                                    json.putAll(s);
                                }
                            });

                            // create rdf model from key-value-paires and remove all paires with empty value
                            // standard namespace
                            String r = domain + "/" + json.get("name").toString();
                            String namespace = domain + "/";
                            // specific namespace from location, if exists in metadata
                            if (!json.get("location").toString().isEmpty()) {
                                r = json.get("location").toString();
                                namespace = r.replace(json.get("name").toString(), "");
                            }
                            String resource = r;

                            json.entrySet().removeIf(entry -> "".equals(entry.getValue()));
                            json.entrySet().removeIf(entry -> entry.getValue() == null);

                            ModelBuilder builder = new ModelBuilder();
                            builder.setNamespace("files", namespace).setNamespace("meta",
                                    domain + "/ontology/metadaten_ontology/");
                            json.forEach((k, v) -> {
                                if (k.equals("type")) {
                                    builder.add(resource, RDF.TYPE, "meta:" + v);
                                } else {
                                    builder.add(resource, "meta:" + k, v);
                                }
                            });
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
                            results += ("Metadaten zur Datei " + resource + " importiert.") + "\n";

                        } catch (IOException e) {
                            log.error(e.getMessage());
                            results += e.getMessage() + "\n";
                        }
                    }
                }
            } catch (MinioException | InvalidKeyException | IllegalArgumentException | NoSuchAlgorithmException
                    | IOException e) {
                log.error("Error occurred: " + e.getMessage());
                results += filename + " - Import failed: " + e.getMessage() + "\n";
            }
        } catch (Exception e) {
            results += "Could not connect to GraphDB. Possibly the database is not available. \n Message: " + e.getMessage();
        }
        return results;
    }

    // import postgres infos from geometries
    public String importPostgresInfos(PostgresInfos infos) {

        String results = "";

        // get connection to graphdb repository
        try {
            RepositoryConnection db = dbconnection.connection(infos.getGraphdbRepo());

            String namespace = domain + "/postgres/";

            // create rdf model from data
            ModelBuilder builder = new ModelBuilder();
            builder.setNamespace("postgres", namespace).setNamespace("geom", "http://geometry.example.org/");
            String object = "postgres:" + infos.getOriginId();
            builder.add(object, "geom:source", infos.getPath() + "/" + infos.getFilename())
            .add(object, "geom:id", infos.getId())
            .add(object, "geom:url", infos.getUrl())
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

        } catch (Exception e) {
            results += "Could not connect to GraphDB. Possibly the database is not available. \n Message: " + e.getMessage();
        }
        return results;
    }

    
}