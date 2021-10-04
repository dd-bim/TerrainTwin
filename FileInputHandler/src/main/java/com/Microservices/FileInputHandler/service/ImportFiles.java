package com.Microservices.FileInputHandler.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.regex.Pattern;

import com.Microservices.FileInputHandler.connection.Csv2RdfConverterConnection;
import com.Microservices.FileInputHandler.connection.GeometryHandlerConnection;
import com.Microservices.FileInputHandler.connection.GraphDBConnection;
import com.Microservices.FileInputHandler.connection.MinIOConnection;
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
import io.minio.UploadObjectArgs;
import io.minio.errors.ErrorResponseException;
import io.minio.errors.InsufficientDataException;
import io.minio.errors.InternalException;
import io.minio.errors.InvalidResponseException;
import io.minio.errors.ServerException;
import io.minio.errors.XmlParserException;
import io.minio.messages.Item;

@Service
public class ImportFiles {

    @Autowired
    MinIOConnection connection;

    @Autowired
    GraphDBConnection dbconnection;

    @Autowired
    GeometryHandlerConnection geoconn;

    @Autowired
    Csv2RdfConverterConnection csvconn;

    @Autowired
    ImportIfc importIfc;

    Logger log = LoggerFactory.getLogger(ImportFiles.class);

    @Value("${minio.url}")
    private String url;
    @Value("${domain.url}")
    private String domain;

    // get files of spezified bucket and import into spezified graphdb repository
    public String getFiles(String bucket, String repo) {

        String results = "";
        String filename = "";

        // get connection to graphdb repository
        try {
            RepositoryConnection db = dbconnection.connection(repo);

            // get connection to Minio
            MinioClient client = connection.connection();

            // Lists objects information
            List<String> bucketlist = new ArrayList<String>();
            try (InputStream stream = client
                    .getObject(GetObjectArgs.builder().bucket(bucket).object("bucketlist.txt").build())) {

                BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
                String line = "";
                while ((line = reader.readLine()) != null) {
                    bucketlist.add(line);
                }

            } catch (Exception e) {
                bucketlist.add("bucketlist.txt");
                log.info("bucketlist.txt didn't exist.");
            }

            int p = 0;
            Iterable<Result<Item>> objects = client.listObjects(ListObjectsArgs.builder().bucket(bucket).build());
            for (Result<Item> result : objects) {
                filename = result.get().objectName();

                if (bucketlist.contains(filename)) {
                    // nothing to do with file
                } else {
                    p++;
                    bucketlist.add(filename);
                    String extension = FilenameUtils.getExtension(filename).toLowerCase();

                    // ignore bucketlist.txt
                    if (filename.equals("bucketlist.txt")) {
                    }

                    // process semantic files
                    else if (extension.equals("ttl") || extension.equals("rdf") || extension.equals("owl")) {

                        results += processSemantics(bucket, filename, extension, db);

                    }

                    // check if file is metadata file
                    else if (extension.equals("json") && filename.contains("metadata")) {

                        results += processMetadata(bucket, filename, db);

                    }

                    // try to import geometries of xml file
                    else if (extension.equals("xml")) {

                        results += geoconn.geometryImport(bucket, repo, filename);

                    }

                    // process file if it is of type ifc
                    else if (extension.equals("ifc")) {

                        results += importIfc.importIfcFile(bucket, repo, filename);

                    }

                    // try to convert txt and csv files to semantic triples
                    // if files contain WKT import geometries into postgres database
                    else if (extension.equals("txt") || extension.equals("csv")) {

                        String[] res = csvconn.convertCsv2Rdf(bucket, filename);
                        results += res[0];
                        if (res[1] == "200") {
                            String ttlFile = filename.split(Pattern.quote("."))[0] + ".ttl";
                            results += processSemantics(bucket, ttlFile, "ttl", db);
                            bucketlist.add(ttlFile);
                        }
                        results += geoconn.geometryImport(bucket, repo, filename);
                    }

                    // unknown file extension
                    else {
                        log.info("The file type " + extension + " is not supported");
                    }
                }
            }

            String fname = "bucketlist.txt";
            File file = new File(fname);
            FileWriter writer = new FileWriter(file);
            for (String bu : bucketlist) {
                writer.write(bu + "\n");
            }
            writer.close();

            client.uploadObject(UploadObjectArgs.builder().bucket(bucket).object(fname).filename(fname).build());
            bucketlist.add(fname);
            // log.info("Updated bucketlist.txt.");

            if (p == 0) {
                results += "Nothing to do.";
                // log.info("Nothing to do.");
            }
        } catch (Exception e) {
            results += "Could not connect to GraphDB. Possibly the database is not available. \n Message: "
                    + e.getMessage();
        }
        return results;
    }

    public String processSemantics(String bucket, String filename, String extension, RepositoryConnection db)
            throws InvalidKeyException, ErrorResponseException, InsufficientDataException, InternalException,
            InvalidResponseException, NoSuchAlgorithmException, ServerException, XmlParserException,
            IllegalArgumentException {
        String results = "";
        MinioClient client = connection.connection();

        // get stream from given bucket and object name
        try (InputStream stream = client.getObject(GetObjectArgs.builder().bucket(bucket).object(filename).build())) {

            String baseURI = url + "/" + bucket + "/" + filename + "/";

            // import turtle file into repository
            if (extension.equals("ttl")) {
                db.add(stream, baseURI, RDFFormat.TURTLE);
            }

            // import rdf/xml or owl file into repository
            if (extension.equals("rdf") || extension.equals("owl")) {
                db.add(stream, baseURI, RDFFormat.RDFXML);
            }

            results += (filename + " imported.") + "\n";

        } catch (IOException e) {
            log.error(e.getMessage());
            results += e.getMessage() + "\n";
        }

        return results;
    }

    public String processMetadata(String bucket, String filename, RepositoryConnection db) throws InvalidKeyException,
            ErrorResponseException, InsufficientDataException, InternalException, InvalidResponseException,
            NoSuchAlgorithmException, ServerException, XmlParserException, IllegalArgumentException {
        String results = "";
        MinioClient client = connection.connection();

        // get stream from given bucket and object name
        try (InputStream stream = client.getObject(GetObjectArgs.builder().bucket(bucket).object(filename).build())) {

            // get key-value-paires from json
            HashMap<String, Object> json = new HashMap<String, Object>();
            @SuppressWarnings("unchecked")
            HashMap<String, Object> obj = new ObjectMapper().readValue(stream, HashMap.class);

            obj.forEach((k, v) -> {
                if (v == null) {
                } else if (v.getClass() == String.class) {
                    json.put(k, v);
                } else {
                    @SuppressWarnings("unchecked")
                    HashMap<String, Object> s = (HashMap<String, Object>) v;
                    json.putAll(s);
                }
            });

            // remove all paires with empty value
            json.entrySet().removeIf(entry -> "".equals(entry.getValue()));
            json.entrySet().removeIf(entry -> entry.getValue() == null);

            // create namespace and resources
            String source = json.get("name").toString();
            String namespace = "";
            if (json.get("location").toString() == null) {
                namespace = url + "/" + bucket + "/";
            } else {
                namespace = json.get("location").toString().replace(source, "");
            }
            String doc = bucket + ":" + source;
            String content = bucket + ":" + UUID.randomUUID().toString();

            // create rdf model from key-value-paires
            ModelBuilder builder = new ModelBuilder();
            builder.setNamespace(bucket, namespace).setNamespace("tto", domain + "/terraintwin/ontology/");
            builder.add(doc, RDF.TYPE, "tto:Document").add(doc, "tto:hasContent", content);
            json.forEach((k, v) -> {
                if (k.equals("type")) {
                    builder.add(content, RDF.TYPE, "tto:" + v);
                } else {
                    builder.add(content, "tto:" + k, v);
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
            results += ("Metadata to file " + source + " imported.") + "\n";

        } catch (IOException e) {
            log.error(e.getMessage());
            results += e.getMessage() + "\n";
        }

        return results;
    }
}