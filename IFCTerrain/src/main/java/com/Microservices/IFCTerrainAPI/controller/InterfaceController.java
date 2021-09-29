package com.Microservices.IFCTerrainAPI.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import com.Microservices.IFCTerrainAPI.domain.model.Filename;
import com.Microservices.IFCTerrainAPI.domain.model.InputConfigs;
import com.Microservices.IFCTerrainAPI.domain.model.UploadInfos;
import com.Microservices.IFCTerrainAPI.service.ExecService;
import com.Microservices.IFCTerrainAPI.service.ProcessFiles;
import com.Microservices.IFCTerrainAPI.service.UploadService;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
public class InterfaceController {

  @Autowired
  UploadService minio;

  @Autowired
  ProcessFiles files;

  @Autowired
  ExecService execService;

  Filename fName = new Filename();
  public static final String BUCKET = "ifctest";

  // start page
  @GetMapping("/terraintoifc")
  public String index(Model model) throws Exception {
    model.addAttribute("erg", "");
    return "index";
  }

  @PostMapping("/terraintoifc")
  public String uploadFiles(@RequestParam("file") MultipartFile multipartFile, @RequestParam String configs, // Map<String,
                                                                                                             // String>
                                                                                                             // data
                                                                                                             // InputConfigs
                                                                                                             // config
      Model model) throws InvalidKeyException, ErrorResponseException, InsufficientDataException, InternalException,
      InvalidResponseException, NoSuchAlgorithmException, ServerException, XmlParserException, IllegalArgumentException,
      IOException, InterruptedException {
    String results = "";
    ObjectMapper mapper = new ObjectMapper().configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);
    InputConfigs config = mapper.readValue(configs, InputConfigs.class);

    File file = minio.multipartToFile(multipartFile);
    UploadInfos infos = new UploadInfos(BUCKET, file);
    results = minio.upload(infos);

    // set destFileName global
    fName.setFilename(config.getDestFileName());

    // set docker internal paths
    config.setFilePath("files/" + config.getFileName());
    config.setDestFileName("files/" + config.getDestFileName());
    config.setLogFilePath("files/");

    // check if data comes from a source file and has a convertable type

    String type = config.getFileType().toUpperCase();
    if (type.equals("0"))
      type = "DXF";
    if (type.equals("LANDXML") || type.equals("1"))
      type = "XML";
    if (type.equals("2") || type.equals("CITYGML"))
      type = "GML";
    if (type.equals("3") || type.equals("GRAFBAT"))
      type = "OUT";
    if (type.equals("5") || type.equals("GRID"))
      type = "XYZ";
    if (type.equals("6"))
      type = "REB";

    if (config.getFileName() != null) {

      // copy source file into container if exists
      results = files.getFileFromMinIO(BUCKET, config.getFileName());

    } else if (config.getHost() != null) {
      // should only be, if data come from PostGIS
      results = "OK";
    } else {
      results = "Problem";
    }

    // check if copy was successful
    if (results.equals("OK")) {

      // create file from configs
      String configFile = files.createConfigFile(config);

      // call converter with config file as input
      results = execService.callConverter(configFile);

      // upload all created files to MinIO bucket
      // if conversion fails, log and configs file will be uploaded
      // results += files.upload(BUCKET, type);
      files.upload(BUCKET, type);

      // delete all created files in container
      files.removeFiles();
    }

    model.addAttribute("erg", results);
    return "index.html";
  }

  @GetMapping("/terraintoifc/download")
  public ResponseEntity<Resource> downloadIFC() throws IOException, InvalidKeyException, ErrorResponseException,
      InsufficientDataException, InternalException, InvalidResponseException, NoSuchAlgorithmException, ServerException,
      XmlParserException, IllegalArgumentException {

    File file = minio.getFile(BUCKET, fName.getFilename());
    Path path = Paths.get(file.getAbsolutePath());
    ByteArrayResource resource = new ByteArrayResource(Files.readAllBytes(path));
    file.delete();

    ContentDisposition contentDisposition = ContentDisposition.builder("inline").filename(fName.getFilename()).build();
    HttpHeaders headers = new HttpHeaders();
    headers.setContentDisposition(contentDisposition);

    return ResponseEntity.ok().headers(headers).contentLength(file.length())
        .contentType(MediaType.APPLICATION_OCTET_STREAM).body(resource);
  }
}

// <form id="myObject" action="whateverYouNeedHere.htm" method="post">
// <input type="hidden" id="items[0].name" name="items[0].name" value="foo"/>
// <input type="hidden" id="items[0].value" name="items[0].value" value="bar"/>