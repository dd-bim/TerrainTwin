package com.Microservices.IFCTerrainAPI.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import com.Microservices.IFCTerrainAPI.connection.MinIOConnection;
import com.Microservices.IFCTerrainAPI.domain.model.InputConfigs;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
public class ProcessFiles {

    @Autowired
    MinIOConnection connection;

    Logger log = LoggerFactory.getLogger(ProcessFiles.class);

    // write input configs into a json file
    public String createConfigFile(InputConfigs configs) throws IOException {
        String name = "";
        if (configs.getFileName() != null) {
            name = configs.getFileName().split("\\.")[0];
        }
        File file = new File("files/" + name + "_configs.json");
        FileWriter writer = new FileWriter(file);
        Gson gson = new GsonBuilder().serializeNulls().create();
        String content = gson.toJson(configs);
        log.info(content);
        writer.write(content);
        writer.close();

        return file.getAbsolutePath();
    }

    // copy source file from MinIO bucket into the container file system
    public String getFileFromMinIO(String bucket, String filenameConfig) throws InvalidKeyException,
            ErrorResponseException, InsufficientDataException, InternalException, InvalidResponseException,
            NoSuchAlgorithmException, ServerException, XmlParserException, IllegalArgumentException {

        String results = "";
        String filename = "";

        // Lists objects information
        try {
            // get connection to Minio
            MinioClient client = connection.connection();

            Iterable<Result<Item>> objects = client.listObjects(ListObjectsArgs.builder().bucket(bucket).build());
            int fileUsed = 0;
            for (Result<Item> result : objects) {
                filename = result.get().objectName();

                if (filename.equals(filenameConfig)) {
                    fileUsed = 1;

                    // get stream from given bucket and object name
                    try (InputStream stream = client
                            .getObject(GetObjectArgs.builder().bucket(bucket).object(filename).build())) {
                        File file = new File("files/" + filename);

                        // wirite file into container
                        FileOutputStream writer = new FileOutputStream(file);
                        writer.write(stream.readAllBytes());
                        writer.close();
                        results = "OK";
                    } catch (IOException e) {
                        log.error(e.getMessage());
                        results += e.getMessage() + "\n";
                    }
                }
            }
        if (fileUsed == 0) results = "Can't find file with name " + filenameConfig;

        } catch (IOException e) {
            log.error("Error occurred: " + e.getMessage());
            results += filename + " - Import failed: " + e.getMessage() + "\n";
        }
        return results;
    }

    // Upload file to given bucket name
    public String upload(String bucket, String type) throws InvalidKeyException, ErrorResponseException,
            InsufficientDataException, InternalException, InvalidResponseException, NoSuchAlgorithmException,
            ServerException, XmlParserException, IllegalArgumentException, IOException {
        MinioClient client = connection.connection();
        String results = "";

        // find all created files in the files folder
        File directory = new File("files");
        String[] flist = directory.list();
        for (int i = 0; i < flist.length; i++) {
            if (!flist[i].toUpperCase().endsWith(type)) {

                // Upload to MinIO
                client.uploadObject(UploadObjectArgs.builder().bucket(bucket).object(flist[i])
                        .filename("files/" + flist[i]).build());
                results += flist[i] + " is successfully uploaded to bucket " + bucket + ".\n";
            }
        }

        return results;
    }

    // remove created files from container
    public void removeFiles() {
        File file = new File("files");
        try {
            FileUtils.cleanDirectory(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
