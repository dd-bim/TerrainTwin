package com.graphdb.connection.connectors;

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
import org.eclipse.rdf4j.rio.RDFFormat;
import org.eclipse.rdf4j.rio.Rio;

import io.github.cdimascio.dotenv.Dotenv;
import io.minio.GetObjectArgs;
import io.minio.ListObjectsArgs;
import io.minio.MinioClient;
import io.minio.Result;
import io.minio.errors.MinioException;
import io.minio.messages.Item;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MinIOConnection {

    MinioClient client;
    // Dotenv env = Dotenv.load();
    Dotenv env = Dotenv.configure().directory("./GraphDBImportService").load();
    public static GraphDBConnection connector = new GraphDBConnection();

    // Connect to MinIO
    public MinIOConnection() {
        client = MinioClient.builder().endpoint(env.get("MINIO_URL") + ":" + env.get("MINIO_PORT"))
                .credentials(env.get("MINIO_ACCESS_KEY"), env.get("MINIO_SECRET_KEY")).build();
    }

    // get files of spezified bucket,
    public String getFiles(String bucket, String repo) throws Exception {
        String results = "";
        String filename = "";

        // Connect to a repository
        RepositoryConnection connection = connector.connect(repo);
        if (connection == null)
            return results += "Could not connect to GraphDB. Possibly the repository does not exist.";

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

                        String baseURI = env.get("MINIO_URL") + "/" + bucket + "/" + filename + "/";

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

                        // get key-value-paires from json and remove all paires with empty value
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
                        json.entrySet().removeIf(entry -> "".equals(entry.getValue()));

                        // create rdf model from key-value-paires
                        ModelBuilder builder = new ModelBuilder();
                        String resource = json.get("location").toString();
                        String namespace = resource.replace(json.get("name").toString(), "");
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