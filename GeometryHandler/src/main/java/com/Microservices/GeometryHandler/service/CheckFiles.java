package com.Microservices.GeometryHandler.service;

import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import com.Microservices.GeometryHandler.connection.MinIOConnection;

import org.apache.commons.io.FilenameUtils;
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
public class CheckFiles {

    @Autowired
    MinIOConnection connection;

    @Autowired
    TextReader readwrite;

    @Autowired
    LandXMLReader readTin;

    @Value("${domain.url}")
    private String domain;

    Logger log = LoggerFactory.getLogger(CheckFiles.class);

    // get files from bucket
    public String getGeoFromBucket(String bucket, String graphdbRepo) throws Exception {
        String results = "";
        MinioClient client = connection.connection();

        // list all files in bucket
        try {
            Iterable<Result<Item>> objects = client.listObjects(ListObjectsArgs.builder().bucket(bucket).build());
            for (Result<Item> result : objects) {
                String filename = result.get().objectName();

                // import geometries
                results += getGeoFromFile(bucket, graphdbRepo, filename);

            }
        } catch (MinioException | InvalidKeyException | IllegalArgumentException | NoSuchAlgorithmException
                | IOException e) {
            log.error("Error occurred: " + e.getMessage());
            results += "\nError occurred: " + e.getMessage();
        }
        return results;
    }

      // get geometry from file and import it
      public String getGeoFromFile(String bucket, String graphdbRepo, String filename) throws Exception {
        String results = "";
        String path = domain + "/minio/" + bucket;
        String extension = FilenameUtils.getExtension(filename);
        MinioClient client = connection.connection();

                // get object from given bucket and object name
                try (InputStream stream = client
                        .getObject(GetObjectArgs.builder().bucket(bucket).object(filename).build())) {
                    // LandXML File
                    if (extension.equals("xml")) {

                        // Insert TIN and Breaklines into database
                        results += "\n" + filename + ": " + readTin.importTIN(stream, path, filename, graphdbRepo);
                    }
                    // text files (csv/txt) with WKT
                    else if (extension.equals("txt") || extension.equals("csv")) {

                        // Insert surfaces into database
                        results += "\n" + filename + ": " + readwrite.importWKT(stream, path, filename, graphdbRepo);
                    }
                } catch (IOException e) {
                    log.error(e.getMessage());
                    results += "\n" + e.getMessage();
                }

        return results;
    }

}