package com.Microservices.MinIOUploadService.service;

import java.io.File;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import com.Microservices.MinIOUploadService.domain.model.UploadInfos;

import org.springframework.web.multipart.MultipartFile;

import io.github.cdimascio.dotenv.Dotenv;
import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.UploadObjectArgs;
import io.minio.errors.ErrorResponseException;
import io.minio.errors.InsufficientDataException;
import io.minio.errors.InternalException;
import io.minio.errors.InvalidResponseException;
import io.minio.errors.ServerException;
import io.minio.errors.XmlParserException;

public class UploadService {

    MinioClient client;
    Dotenv env = Dotenv.configure().directory("./MinioUploadService").load();

    // Connect to MinIO
    public UploadService() {
        client = MinioClient.builder().endpoint(env.get("MINIO_URL") + ":" + env.get("MINIO_PORT"))
                .credentials(env.get("MINIO_ACCESS_KEY"), env.get("MINIO_SECRET_KEY")).build();
    }

    public String upload(UploadInfos infos) throws InvalidKeyException, ErrorResponseException,
            InsufficientDataException, InternalException, InvalidResponseException, NoSuchAlgorithmException,
            ServerException, XmlParserException, IllegalArgumentException, IOException {
        String results = "";
        String bucket = infos.getBucket();
        String path = infos.getPath().replaceAll("\\\\", "/");
        String fn[] = path.split("/");
        String filename = fn[fn.length - 1];

        // Make bucket if not exist.
        boolean found = client.bucketExists(BucketExistsArgs.builder().bucket(bucket).build());
        if (!found) {
            // Make a new bucket.
            results = createBucket(bucket);
        } else {
            results = "Bucket " + bucket + " already exists.\n";
        }

        client.uploadObject(UploadObjectArgs.builder().bucket(bucket).object(filename).filename(path).build());
        results += filename + " is successfully uploaded to bucket " + bucket + ".";

        return results;
    }

    public String createBucket(String bucket) throws InvalidKeyException, ErrorResponseException,
            InsufficientDataException, InternalException, InvalidResponseException, NoSuchAlgorithmException,
            ServerException, XmlParserException, IllegalArgumentException, IOException {
        client.makeBucket(MakeBucketArgs.builder().bucket(bucket).build());
        String results = "Bucket " + bucket + " created.\n";
        return results;
    }

    public File multipartToFile(MultipartFile file) throws IllegalStateException, IOException {
        File convFile = new File(System.getProperty("java.io.tmpdir") + "/" + file.getOriginalFilename());
        file.transferTo(convFile);
        return convFile;
    }
}
