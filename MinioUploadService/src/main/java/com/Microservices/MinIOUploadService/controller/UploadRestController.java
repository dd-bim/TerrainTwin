package com.Microservices.MinIOUploadService.controller;

import java.io.File;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

import com.Microservices.MinIOUploadService.connection.MinIOConnection;
import com.Microservices.MinIOUploadService.domain.model.DTM;
import com.Microservices.MinIOUploadService.domain.model.MetaFile;
import com.Microservices.MinIOUploadService.domain.model.Metadata;
import com.Microservices.MinIOUploadService.domain.model.UploadInfos;
import com.Microservices.MinIOUploadService.service.UploadService;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import io.minio.errors.ErrorResponseException;
import io.minio.errors.InsufficientDataException;
import io.minio.errors.InternalException;
import io.minio.errors.InvalidResponseException;
import io.minio.errors.ServerException;
import io.minio.errors.XmlParserException;
import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@RefreshScope
@RestController
@Tag(name = "MinIO Uploader", description = "Upload files and metadata to the MinIO Object Storage", externalDocs = @ExternalDocumentation(url = "/minioupload", description = "Interface"))
public class UploadRestController {

  @Value("${minio.url}")
  private String url;
  @Value("${minio.port}")
  private String port;
  @Value("${minio.access_key}")
  private String access_key;
  @Value("${minio.secret_key}")
  private String secret_key;

  // https://docs.aws.amazon.com/AmazonS3/latest/userguide/bucketnamingrules.html

  @GetMapping("/minioupload/createbucket/{bucket}")
  @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content)
  public ResponseEntity<?> create(@PathVariable String bucket) throws Exception {
    if (bucket.matches("^[a-z0-9][a-z0-9-.]{1,61}[a-z0-9]$")) {
      MinIOConnection connect = new MinIOConnection();
      UploadService minio = new UploadService(connect.connection(url, port, access_key, secret_key));
      String results = minio.createBucket(bucket);
      return ResponseEntity.ok(results);
    } else {
      ;
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
          "The bucket name is not valid. The name must be at least 3 characters long, start and end with a digit or lowercase letter and contain only 'a-z', '0-9', '.' and '-''. More than one dot or hyphen in a row is also not allowed.");
    }
  }

  @GetMapping("/minioupload/deletebucket/{bucket}")
  @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content)
  public ResponseEntity<?> delete(@PathVariable String bucket) throws Exception {
    if (bucket.matches("^[a-z0-9][a-z0-9-.]{1,61}[a-z0-9]$")) {
      MinIOConnection connect = new MinIOConnection();
      UploadService minio = new UploadService(connect.connection(url, port, access_key, secret_key));
      String results = minio.deleteBucket(bucket);
      return ResponseEntity.ok(results);
    } else {
      ;
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
          "The bucket name is not valid. The name must be at least 3 characters long, start and end with a digit or lowercase letter and contain only 'a-z', '0-9', '.' and '-''. More than one dot or hyphen in a row is also not allowed.");
    }
  }

  @PostMapping(path = "/minioupload/upload", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
  @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content)
  @Operation(summary = "Upload a file with metadata.", description = "If type is DTM use the metadata in DIN 18740-6 additionally, else ignore them. <br><br> All metadata in DIN SPEC 91391-2 and DIN 18740-6 are optional.")
  @ApiResponse(responseCode = "200", content = { @Content(schema = @Schema(implementation = MetaFile.class)) })
  // @ApiResponse(responseCode = "200", content = {@Content(schema =
  // @Schema(implementation = Metadata.class))})
  // @ApiResponse(responseCode = "200", content = {@Content(schema =
  // @Schema(implementation = DTM.class))})
  public ResponseEntity<?> uploadFileUI(@RequestParam("file") MultipartFile multiFile, @RequestParam String bucket,
      @RequestPart(name = "metadata for all (DIN SPEC 91391-2)") String meta1,
      @RequestPart(name = "additional metadata for dtm (DIN 18740-6)", required = false) String meta2)
      throws InvalidKeyException, ErrorResponseException, InsufficientDataException, InternalException,
      InvalidResponseException, NoSuchAlgorithmException, ServerException, XmlParserException, IllegalArgumentException,
      IOException {
    if (bucket.matches("^[a-z0-9][a-z0-9-.]{1,61}[a-z0-9]$")) {
      MinIOConnection connect = new MinIOConnection();
      UploadService minio = new UploadService(connect.connection(url, port, access_key, secret_key));
      String results = "";
      ObjectMapper mapper = new ObjectMapper();
      Metadata metadata = mapper.readValue(meta1, Metadata.class);

      // create buckets and upload files with metadata
      File file = minio.multipartToFile(multiFile);
      UploadInfos infos = new UploadInfos(bucket, file);
      results = minio.upload(infos);

      metadata.setCreated(infos.getTimestamp());
      metadata.setName(infos.getTimestamp() + "_" + file.getName());
      metadata.setLocation(
          "https://terrain.dd-bim.org" + "/minio/" + bucket + "/" + infos.getTimestamp() + "_" + file.getName());
      metadata.setId(UUID.randomUUID());

      String ext = file.getName().split("\\.")[1];
      switch (ext) {
        case "ifc":
          metadata.setMimetype("application/x-step");
          break;
        case "dwg":
          metadata.setMimetype("application/acad");
          break;
        case "dxf":
          metadata.setMimetype("application/dxf");
          break;
        case "gml":
          metadata.setMimetype("application/gml+xml");
          break;
        case "ttl":
          metadata.setMimetype("text/turtle");
          break;
        case "owl":
          metadata.setMimetype("application/rdf+xml");
          break;
        case "xml":
          metadata.setMimetype("application/xml");
          break;
        default:
          metadata.setMimetype("");
      }

      MetaFile metaFile;

      // if file is a DTM, get DTM metadata and add to upload
      if (metadata.getType().equals("DTM") && meta2 != null) {
        DTM dtm = mapper.readValue(meta2, DTM.class);
        metaFile = new MetaFile(metadata, dtm);

      } else {
        metaFile = new MetaFile(metadata);
      }

      // make JSON file from metadata
      File metadataFile = minio.metaToJson(metaFile, file.getName());
      // upload JSON file
      UploadInfos metaInfos = new UploadInfos(bucket, metadataFile);
      results += minio.upload(metaInfos);

      return ResponseEntity.ok(results);
    } else {
      ;
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
          "The bucket name is not valid. The name must be at least 3 characters long, start and end with a digit or lowercase letter and contain only 'a-z', '0-9', '.' and '-''. More than one dot or hyphen in a row is also not allowed.");
    }
  }

  @PostMapping(path = "/minioupload/uploadFile", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  @Operation(summary = "Upload a file to MinIO without metadata.")
  @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content)
  public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile multiFile, @RequestParam String bucket)
      throws InvalidKeyException, ErrorResponseException, InsufficientDataException, InternalException,
      InvalidResponseException, NoSuchAlgorithmException, ServerException, XmlParserException, IllegalArgumentException,
      IOException {
    if (bucket.matches("^[a-z0-9][a-z0-9-.]{1,61}[a-z0-9]$")) {
      MinIOConnection connect = new MinIOConnection();
      UploadService minio = new UploadService(connect.connection(url, port, access_key, secret_key));
      String results = "";

      File file = minio.multipartToFile(multiFile);
      UploadInfos infos = new UploadInfos(bucket, file);
      results = minio.upload(infos);

      return ResponseEntity.ok(results);
    } else {
      ;
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
          "The bucket name is not valid. The name must be at least 3 characters long, start and end with a digit or lowercase letter and contain only 'a-z', '0-9', '.' and '-''. More than one dot or hyphen in a row is also not allowed.");
    }
  }

}