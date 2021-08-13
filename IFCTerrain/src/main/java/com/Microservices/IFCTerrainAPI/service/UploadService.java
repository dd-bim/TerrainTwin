package com.Microservices.IFCTerrainAPI.service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.Microservices.IFCTerrainAPI.connection.MinIOConnection;
import com.Microservices.IFCTerrainAPI.domain.model.InputConfigs;
import com.Microservices.IFCTerrainAPI.domain.model.UploadInfos;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import io.minio.BucketExistsArgs;
import io.minio.GetObjectArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.UploadObjectArgs;
import io.minio.errors.ErrorResponseException;
import io.minio.errors.InsufficientDataException;
import io.minio.errors.InternalException;
import io.minio.errors.InvalidResponseException;
import io.minio.errors.ServerException;
import io.minio.errors.XmlParserException;

@Service
public class UploadService {

    @Autowired
    MinIOConnection connection;

    // Upload file to given bucket name
    public String upload(UploadInfos infos) throws InvalidKeyException, ErrorResponseException,
            InsufficientDataException, InternalException, InvalidResponseException, NoSuchAlgorithmException,
            ServerException, XmlParserException, IllegalArgumentException, IOException {
        MinioClient client = connection.connection();
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

        // Upload to MinIO
        client.uploadObject(UploadObjectArgs.builder().bucket(bucket).object(filename).filename(path).build());
        results += filename + " is successfully uploaded to bucket " + bucket + ".\n";

        return results;
    }

    // create new bucket
    public String createBucket(String bucket) throws InvalidKeyException, ErrorResponseException,
            InsufficientDataException, InternalException, InvalidResponseException, NoSuchAlgorithmException,
            ServerException, XmlParserException, IllegalArgumentException, IOException {
        MinioClient client = connection.connection();
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
    public File configToJson(InputConfigs configs) throws JsonGenerationException, JsonMappingException, IOException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        String timestamp = format.format(new Date()).replaceAll(":", "-");
        File file = new File("files/" + "configs_" + timestamp + ".json");
        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(file, configs);

        return file;
    }

    // download the ifc file from MinIO into container
    public File getFile(String bucket, String filename) throws InvalidKeyException, ErrorResponseException,
            InsufficientDataException, InternalException, InvalidResponseException, NoSuchAlgorithmException,
            ServerException, XmlParserException, IllegalArgumentException {
        MinioClient client = connection.connection();
        File file = new File("files/" + filename);
        try (InputStream stream = client.getObject(GetObjectArgs.builder().bucket(bucket).object(filename).build())) {

            Files.copy(stream, file.toPath(), StandardCopyOption.REPLACE_EXISTING);

        } catch (IOException e) {
            System.out.println("MinIO Download Error: " + e.getMessage());
        }

        return file;
    }
}
