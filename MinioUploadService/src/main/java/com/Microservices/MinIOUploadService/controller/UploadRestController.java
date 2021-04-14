package com.Microservices.MinIOUploadService.controller;

import java.io.File;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

import com.Microservices.MinIOUploadService.domain.model.MetaFile;
import com.Microservices.MinIOUploadService.domain.model.Metadata;
import com.Microservices.MinIOUploadService.domain.model.Upload;
import com.Microservices.MinIOUploadService.domain.model.UploadInfos;
import com.Microservices.MinIOUploadService.service.UploadService;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.minio.errors.ErrorResponseException;
import io.minio.errors.InsufficientDataException;
import io.minio.errors.InternalException;
import io.minio.errors.InvalidResponseException;
import io.minio.errors.ServerException;
import io.minio.errors.XmlParserException;
import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@RefreshScope
@RestController
@RequestMapping("/terraintwin")
@CrossOrigin(origins = {"http://localhost:8084", "http://localhost:7204"})
@Tag(name = "MinIO Uploader", description = "Upload files and metadata to the MinIO Object Storage", externalDocs = @ExternalDocumentation(url = "/terraintwin/minioupload", description = "Interface"))
public class UploadRestController {

  @Value("${minio.url}")
  private String url;
  @Value("${minio.port}")
  private String port;
  @Value("${minio.access_key}")
  private String access_key;
  @Value("${minio.secret_key}")
  private String secret_key;

  @GetMapping("/minioupload/createbucket/{bucket}")
  // @Operation(security= @SecurityRequirement(name = "basicAuth"))
  public String create(@PathVariable String bucket) throws Exception {
    UploadService minio = new UploadService(url, port, access_key, secret_key);
    String results = minio.createBucket(bucket);
    return results;
  }

  @GetMapping("/minioupload/deletebucket/{bucket}")
  public String delete(@PathVariable String bucket) throws Exception {
    UploadService minio = new UploadService(url, port, access_key, secret_key);
    String results = minio.deleteBucket(bucket);
    return results;
  }

  @PostMapping(path = "/minioupload/upload")
  @Operation(summary = "Upload a file with metadata.",
  description = "If type is DTM use the metadata in DIN 18740-6 additionally, else ignore them. <br><br> All metadata in DIN SPEC 91391-2 and DIN 18740-6 are optional.")
  // , security= @SecurityRequirement(name = "basicAuth")
  public String uploadFileUI(@RequestBody Upload meta) throws InvalidKeyException, ErrorResponseException,
      InsufficientDataException, InternalException, InvalidResponseException, NoSuchAlgorithmException, ServerException,
      XmlParserException, IllegalArgumentException, IOException {
    UploadService minio = new UploadService(url, port, access_key, secret_key);
    String results = "";

    // create buckets and upload files with metadata
    Metadata metadata = meta.getMetadata();
    String bucket = meta.getBucket();
    File file = meta.getFile();
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
    if (metadata.getType().equals("DTM")) {
      metaFile = new MetaFile(metadata, meta.getDtm());
    } else {
      metaFile = new MetaFile(metadata);
    }

    // make JSON file from metadata
    File metadataFile = minio.metaToJson(metaFile, file.getName());
    // upload JSON file
    UploadInfos metaInfos = new UploadInfos(bucket, metadataFile);
    results += minio.upload(metaInfos);

    return results;
  }

  @PostMapping(path = "/minioupload/uploadFile")
  @Operation(summary = "Upload a file to MinIO without metadata.")
  public String uploadFile(@RequestParam("file") String filepath, @RequestParam String bucket)
      throws InvalidKeyException, ErrorResponseException, InsufficientDataException, InternalException,
      InvalidResponseException, NoSuchAlgorithmException, ServerException, XmlParserException, IllegalArgumentException,
      IOException {
    UploadService minio = new UploadService(url, port, access_key, secret_key);
    String results = "";

    File file = new File(filepath);
    UploadInfos infos = new UploadInfos(bucket, file);
    results = minio.upload(infos);
    return results;
  }

}