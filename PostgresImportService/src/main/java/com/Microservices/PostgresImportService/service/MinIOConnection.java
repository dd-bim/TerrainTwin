package com.Microservices.PostgresImportService.service;

import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import com.Microservices.PostgresImportService.repositories.BreaklinesRepository;
import com.Microservices.PostgresImportService.repositories.SurfaceRepository;
import com.Microservices.PostgresImportService.repositories.TINRepository;

import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.minio.GetObjectArgs;
import io.minio.ListObjectsArgs;
import io.minio.MinioClient;
import io.minio.Result;
import io.minio.errors.MinioException;
import io.minio.messages.Item;

public class MinIOConnection {

    Logger log = LoggerFactory.getLogger(MinIOConnection.class);

    // Connect to MinIO server
    MinioClient client;
    public MinIOConnection(String url, String port, String access_key, String secret_key) {
        client = MinioClient.builder().endpoint(url + ":" + port)
                .credentials(access_key, secret_key).build();
    }


    // get files of spezified bucket,
    public String getFiles(String bucket, TINRepository tinRepository, BreaklinesRepository blRepository,
            SurfaceRepository repository) throws Exception {
        String results = "";
        String filename = "";

        // Lists objects information.
        try {
            Iterable<Result<Item>> objects = client.listObjects(ListObjectsArgs.builder().bucket(bucket).build());
            for (Result<Item> result : objects) {
                filename = result.get().objectName();
                String extension = FilenameUtils.getExtension(filename);

                // LandXML File
                if (extension.equals("xml")) {

                    // get object given the bucket and object name
                    try (InputStream XMLStream = client
                            .getObject(GetObjectArgs.builder().bucket(bucket).object(filename).build())) {

                        // Insert TIN and Breaklines into database
                        ImportLandXML readTin = new ImportLandXML();
                        results += "\n" + filename + ": " + readTin.importTIN(XMLStream, tinRepository, blRepository);

                    } catch (IOException e) {
                        log.error(e.getMessage());
                        results += "\n" + e.getMessage();
                    }
                }

                // File with surfaces
                if (extension.equals("txt") || extension.equals("csv")) {

                    try (InputStream TXTStream = client
                            .getObject(GetObjectArgs.builder().bucket(bucket).object(filename).build())) {

                        // Insert surfaces into database
                        ReadWrite readwrite = new ReadWrite();
                        results += "\n" + filename + ": " + readwrite.importWKT(TXTStream, repository);

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