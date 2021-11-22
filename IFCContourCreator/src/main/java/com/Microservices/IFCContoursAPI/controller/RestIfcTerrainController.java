package com.Microservices.IFCContoursAPI.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.Microservices.IFCContoursAPI.service.ExecService;
import com.Microservices.IFCContoursAPI.service.ProcessFiles;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "Contour Extractor", description = "Get contour from IFC model")
public class RestIfcTerrainController {

  @Autowired
  ProcessFiles files;

  @Autowired
  ExecService execService;

  Logger log = LoggerFactory.getLogger(RestIfcTerrainController.class);

  public static final String CPATH = "files/";

  // send file and configs to IFCTerrainCommand
  @PostMapping(path = "/ifccontour-api/create", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
  @Operation(summary = "Create contour from IFC")
  public ResponseEntity<?> createContoursApi(@RequestParam("file") MultipartFile multipartFile)
      throws IllegalStateException, IOException, InterruptedException {
    String results = null;

    // clean folder from old files
    files.removeFiles();

    // copy file into container and get filename
    String filename = files.multipartToFile(multipartFile);

    // call IfcContourExtractor with file as input
    results = execService.callConverter(CPATH + filename);

    if (results.contains("Successful conversion.")) {

      String outputName = filename.split("\\.")[0] + "_ifc_WKT_contour.txt";
      
      // if conversion is successful download ifc file
      File file = new File(CPATH + outputName);
      Path path = Paths.get(CPATH + outputName);
      ByteArrayResource resource = new ByteArrayResource(Files.readAllBytes(path));

      ContentDisposition contentDisposition = ContentDisposition.builder("inline").filename(outputName).build();
      HttpHeaders headers = new HttpHeaders();
      headers.setContentDisposition(contentDisposition);

      return ResponseEntity.ok().headers(headers).contentLength(file.length())
          .contentType(MediaType.APPLICATION_OCTET_STREAM).body(resource);
    } else {
      // if conversion fails open log file and print logs
      File dir = new File(CPATH);
      String[] logfile = dir.list((d, s) -> {
        return s.toLowerCase().endsWith(".log");
      });

      BufferedReader reader = new BufferedReader(new FileReader(CPATH + logfile[0]));
      String line;
      while ((line = reader.readLine()) != null) {
        results += line + "\n";
      }

      reader.close();
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(results);
    }

  }
}