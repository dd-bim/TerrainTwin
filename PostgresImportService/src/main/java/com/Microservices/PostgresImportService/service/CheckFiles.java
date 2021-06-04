package com.Microservices.PostgresImportService.service;

import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import com.Microservices.PostgresImportService.connection.MinIOConnection;

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

// check, which files are in bucket
@Service
public class CheckFiles {

    @Autowired
    MinIOConnection connection;

    @Autowired
    TextReader readwrite;

    @Autowired
    LandXMLReader readTin;

    @Value("${minio.url}")
    private String url;
    
    Logger log = LoggerFactory.getLogger(CheckFiles.class);

    // get files of spezified bucket,
    public String getFiles(String bucket, String graphdbRepo) throws Exception {
        String results = "";
        String filename = "";
        MinioClient client = connection.connection();

        // Lists objects information.
        try {
            Iterable<Result<Item>> objects = client.listObjects(ListObjectsArgs.builder().bucket(bucket).build());
            for (Result<Item> result : objects) {
                filename = result.get().objectName();
                String path = url + "/" + bucket;
                String extension = FilenameUtils.getExtension(filename);

                // LandXML File
                if (extension.equals("xml")) {

                    // get object given the bucket and object name
                    try (InputStream XMLStream = client
                            .getObject(GetObjectArgs.builder().bucket(bucket).object(filename).build())) {

                        // Insert TIN and Breaklines into database
                        results += "\n" + filename + ": " + readTin.importTIN(XMLStream);

                    } catch (IOException e) {
                        log.error(e.getMessage());
                        results += "\n" + e.getMessage();
                    }
                }

                // text files (csv/txt) with WKT
                if (extension.equals("txt") || extension.equals("csv")) {

                    try (InputStream TXTStream = client
                            .getObject(GetObjectArgs.builder().bucket(bucket).object(filename).build())) {

                        // Insert surfaces into database
                        results += "\n" + filename + ": " + readwrite.importWKT(TXTStream, path, filename, graphdbRepo);

                    } catch (IOException e) {
                        log.error(e.getMessage());
                        results += "\n" + e.getMessage();
                    }

                }
            }
        } catch (MinioException | InvalidKeyException | IllegalArgumentException | NoSuchAlgorithmException
                | IOException e) {
            log.error("Error occurred: " + e.getMessage());
            results += "\n" + filename + " - Error: " + e.getMessage();
        }
        return results;
    }

}