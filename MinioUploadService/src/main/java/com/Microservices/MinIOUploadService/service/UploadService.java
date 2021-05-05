package com.Microservices.MinIOUploadService.service;

import java.io.File;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import com.Microservices.MinIOUploadService.domain.model.MetaFile;
import com.Microservices.MinIOUploadService.domain.model.UploadInfos;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.web.multipart.MultipartFile;

import io.minio.BucketExistsArgs;
import io.minio.ListObjectsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.RemoveBucketArgs;
import io.minio.RemoveObjectArgs;
import io.minio.Result;
import io.minio.UploadObjectArgs;
import io.minio.errors.ErrorResponseException;
import io.minio.errors.InsufficientDataException;
import io.minio.errors.InternalException;
import io.minio.errors.InvalidResponseException;
import io.minio.errors.ServerException;
import io.minio.errors.XmlParserException;
import io.minio.messages.Item;

public class UploadService {

    MinioClient client;

    public UploadService(MinioClient client) {
        this.client = client;
    }

    // Upload file to given bucket name
    public String upload(UploadInfos infos) throws InvalidKeyException, ErrorResponseException,
            InsufficientDataException, InternalException, InvalidResponseException, NoSuchAlgorithmException,
            ServerException, XmlParserException, IllegalArgumentException, IOException {
        String results = "";
        String bucket = infos.getBucket();
        String path = infos.getPath().replaceAll("\\\\", "/");
        System.out.println(path);
        if (path.startsWith("/"))
            path = path.replaceFirst("/", "");
        String fn[] = path.split("/");
        String filename = infos.getTimestamp() + "_" + fn[fn.length - 1];

        // Make bucket if not exist.
        boolean found = client.bucketExists(BucketExistsArgs.builder().bucket(bucket).build());
        if (!found) {
            // Make a new bucket.
            results = createBucket(bucket);
        } else {
            results = "Bucket " + bucket + " already exists.\n";
        }

        // Upload to MinIO
        client.uploadObject(UploadObjectArgs.builder().bucket(bucket).object(filename).filename(path).build());
        results += filename + " is successfully uploaded to bucket " + bucket + ".\n";

        return results;
    }

    // delete bucket and it's files
    public String deleteBucket(String bucket) {
        String results = "";
        try {
            Iterable<Result<Item>> objects = client.listObjects(ListObjectsArgs.builder().bucket(bucket).build());
            for (Result<Item> obj : objects) {
                client.removeObject(RemoveObjectArgs.builder().bucket(bucket).object(obj.get().objectName()).build());
                results += obj.get().objectName() + " removed.\n";
            }
        } catch (Exception e) {
            results += "Couldn't remove objects from bucket " + bucket + ".\n" + e;
        }
        try {
            client.removeBucket(RemoveBucketArgs.builder().bucket(bucket).build());
            results += "Bucket " + bucket + " removed.\n";
        } catch (Exception e) {
            results += "Couldn't remove bucket" + bucket + ".\n" + e;
        }
        return results;
    }

    // create new bucket
    public String createBucket(String bucket) throws InvalidKeyException, ErrorResponseException,
            InsufficientDataException, InternalException, InvalidResponseException, NoSuchAlgorithmException,
            ServerException, XmlParserException, IllegalArgumentException, IOException {
        client.makeBucket(MakeBucketArgs.builder().bucket(bucket).build());
        String results = "Bucket " + bucket + " created.\n";
        return results;
    }

    // convert multipart class file to java class file
    public File multipartToFile(MultipartFile file) throws IllegalStateException, IOException {
        File convFile = new File(System.getProperty("java.io.tmpdir") + "/" + file.getOriginalFilename());
        file.transferTo(convFile);
        return convFile;
    }

    // create temporary JSON file from metadata
    public File metaToJson(MetaFile metaFile, String filename)
            throws JsonGenerationException, JsonMappingException, IOException {
        File file = new File(System.getProperty("java.io.tmpdir") + "/" + filename.split("\\.")[0] + "_metadata.json");
        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(file, metaFile);

        return file;
    }
}
