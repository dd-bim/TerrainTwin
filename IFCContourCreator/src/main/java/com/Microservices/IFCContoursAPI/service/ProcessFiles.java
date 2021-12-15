package com.Microservices.IFCContoursAPI.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import com.Microservices.IFCContoursAPI.connection.MinIOConnection;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import io.minio.GetObjectArgs;
import io.minio.MinioClient;
import io.minio.UploadObjectArgs;
import io.minio.errors.ErrorResponseException;
import io.minio.errors.InsufficientDataException;
import io.minio.errors.InternalException;
import io.minio.errors.InvalidResponseException;
import io.minio.errors.ServerException;
import io.minio.errors.XmlParserException;

@Service
public class ProcessFiles {

    @Autowired
    MinIOConnection connection;

    Logger log = LoggerFactory.getLogger(ProcessFiles.class);

    public static final String CPATH = "files/";

    // convert multipart class file to java class file
    public String multipartToFile(MultipartFile file) throws IllegalStateException, IOException {
        File sourceFile = new File(System.getProperty("java.io.tmpdir") + "/" + file.getOriginalFilename());
        file.transferTo(sourceFile);
        File convFile = new File(CPATH + file.getOriginalFilename());
        Files.copy(sourceFile.toPath(), convFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
        return file.getOriginalFilename();
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

     // get file from MinIO bucket
     public File getSourceFile(String bucket, String filename) {
        MinioClient client = connection.connection();
        File file = new File(CPATH + filename);
        try (InputStream stream = client.getObject(GetObjectArgs.builder().bucket(bucket).object(filename).build())) {

            OutputStream outputStream = new FileOutputStream(file);
            IOUtils.copy(stream, outputStream);
            outputStream.close();

        } catch (Exception e) {
            log.info("Couldn't read source file.");
        }
        return file;
    }

    // Upload to MinIO
    public void uploadFile(String bucket, String filename) throws InvalidKeyException, ErrorResponseException, InsufficientDataException, InternalException, InvalidResponseException, NoSuchAlgorithmException, ServerException, XmlParserException, IllegalArgumentException, IOException{
        MinioClient client = connection.connection();
        client.uploadObject(UploadObjectArgs.builder().bucket(bucket).object(filename).filename(CPATH + filename).build());
        // return true;
    }
}
