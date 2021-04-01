package com.Microservices.GraphDBImportService.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.commons.io.FilenameUtils;
import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.model.util.ModelBuilder;
import org.eclipse.rdf4j.model.vocabulary.RDF;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.eclipse.rdf4j.repository.manager.RemoteRepositoryManager;
import org.eclipse.rdf4j.rio.RDFFormat;
import org.eclipse.rdf4j.rio.Rio;

import io.minio.GetObjectArgs;
import io.minio.ListObjectsArgs;
import io.minio.MinioClient;
import io.minio.Result;
import io.minio.errors.MinioException;
import io.minio.messages.Item;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ImportService {

    MinioClient client;
    RepositoryConnection connection;
    RemoteRepositoryManager manager;

    // Connect to MinIO and GraphDB
    public ImportService(String url, String port, String access_key, String secret_key, String graphdb_url,
            String graphdb_username, String graphdb_password) {
        client = MinioClient.builder().endpoint(url + ":" + port).credentials(access_key, secret_key).build();

        manager = new RemoteRepositoryManager(graphdb_url);
        manager.setUsernameAndPassword(graphdb_username, graphdb_password);
        manager.init();
    }

    // get files of spezified bucket,
    public String getFiles(String bucket, String repo, String url) throws Exception {
        String results = "";
        String filename = "";

        // Connect to a repository
        try {
            connection = manager.getRepository(repo).getConnection();
        } catch (Exception e) {
            return results += "Could not connect to GraphDB. Possibly the repository does not exist.";
        }

        // Lists objects information
        try {
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
                            connection.add(stream, baseURI, RDFFormat.TURTLE);
                        }

                        // import rdf/xml or owl file into repository
                        if (extension.equals("rdf") || extension.equals("owl")) {
                            connection.add(stream, baseURI, RDFFormat.RDFXML);
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
                        String r = "https://terrain.dd-bim.org/" + json.get("name").toString();
                        String namespace = "https://terrain.dd-bim.org/";
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
                                "https://terrain.dd-bim.org/ontology/metadaten_ontology/");
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
                        connection.add(tmp, namespace, RDFFormat.TURTLE);
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
        return results;
    }
}