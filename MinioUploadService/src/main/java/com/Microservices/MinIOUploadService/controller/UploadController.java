package com.Microservices.MinIOUploadService.controller;

import java.io.File;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

import com.Microservices.MinIOUploadService.connection.MinIOConnection;
import com.Microservices.MinIOUploadService.domain.model.DTM;
import com.Microservices.MinIOUploadService.domain.model.MetaFile;
import com.Microservices.MinIOUploadService.domain.model.Metadata;
import com.Microservices.MinIOUploadService.domain.model.UploadInfos;
import com.Microservices.MinIOUploadService.service.UploadService;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import io.minio.errors.ErrorResponseException;
import io.minio.errors.InsufficientDataException;
import io.minio.errors.InternalException;
import io.minio.errors.InvalidResponseException;
import io.minio.errors.ServerException;
import io.minio.errors.XmlParserException;

@Controller
@CrossOrigin(origins = "http://localhost:8084")
public class UploadController {

  @Value("${minio.url}")
  private String url;
  @Value("${minio.port}")
  private String port;
  @Value("${minio.access_key}")
  private String access_key;
  @Value("${minio.secret_key}")
  private String secret_key;

  // start page
  @GetMapping("/minioupload")
  public String index(Model model) throws Exception {
    model.addAttribute("erg", "");
    return "index";
  }

  @PostMapping("/minioupload")
  public String uploadFileUI(@RequestParam("file") MultipartFile multipartFile, @RequestParam Map<String, String> data,
      Model model) throws InvalidKeyException, ErrorResponseException, InsufficientDataException, InternalException,
      InvalidResponseException, NoSuchAlgorithmException, ServerException, XmlParserException, IllegalArgumentException,
      IOException {
    MinIOConnection connect = new MinIOConnection();
    UploadService minio = new UploadService(connect.connection(url, port, access_key, secret_key));
    String results = "";
    String action = data.get("action");
    String bucket = data.get("bucket");

    // create buckets and upload files with metadata
    if (action.equals("upload")) {
      File file = minio.multipartToFile(multipartFile);
      UploadInfos infos = new UploadInfos(bucket, file);
      results = minio.upload(infos);

      // if checkbox for metadata file is checked, get metadata
      if (data.get("metachecker").equals("on")) {
        MetaFile metaFile;

        Metadata metadata = new Metadata(bucket, file.getName(), infos.getTimestamp(), data.get("type"),
            data.get("description"), data.get("creator"), data.get("sender"), data.get("recipients"),
            data.get("suitability"), data.get("revision"), data.get("version"), data.get("status"),
            data.get("projectId"), data.get("metadataSchema"), data.get("schema"), data.get("schemaVersion"),
            data.get("schemaSubset"));

        // if file is a DTM, get DTM metadata and add to upload
        if (data.get("type").equals("DTM")) {
          DTM dtm = new DTM(data.get("aktualitaet"), data.get("koordLage"), data.get("koordHoehe"),
              data.get("projektion"), data.get("ausdehnung"), data.get("datenstruktur"), data.get("darstellungsform"),
              data.get("erfassungsmethode"), data.get("messGenauigkeit"), data.get("innereGenauigkeit"),
              data.get("aeussereGenauigkeit"));

          metaFile = new MetaFile(metadata, dtm);
        } else {
          metaFile = new MetaFile(metadata);
        }

        // make JSON file from metadata
        File metadataFile = minio.metaToJson(metaFile, file.getName());

        // upload JSON file
        UploadInfos metaInfos = new UploadInfos(bucket, metadataFile);
        results += minio.upload(metaInfos);

      }

    }

    // delete bucket with files
    if (action.equals("delete")) {
      results = minio.deleteBucket(bucket);
    }

    model.addAttribute("erg", results);
    return "index.html";
  }
}