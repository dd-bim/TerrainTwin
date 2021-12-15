package com.Microservices.IFCContoursAPI.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import io.minio.errors.ErrorResponseException;
import io.minio.errors.InsufficientDataException;
import io.minio.errors.InternalException;
import io.minio.errors.InvalidResponseException;
import io.minio.errors.ServerException;
import io.minio.errors.XmlParserException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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

  // send file to IfcGeometryExtractor and give WKT file back
  @PostMapping(path = "/createFromFile", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
  @Operation(summary = "Create contour from uploaded IFC file")
  public ResponseEntity<?> createContourFromFile(@RequestParam("file") MultipartFile multipartFile)
      throws IllegalStateException, IOException, InterruptedException {
    String results = null;

    // clean folder from old files
    files.removeFiles();

    // copy file into container and get filename
    String filename = files.multipartToFile(multipartFile);

    // call IfcContourExtractor with file as input
    results = execService.callConverter(CPATH + filename);

    if (results.contains("Successful creation.")) {

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
      // if creation fails open log file and print logs
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

  // send IFC file downloaded from MinIO to IfcGeometryExtractor and get resulting
  // WKT string back
  @GetMapping("/createFromStorage/minio/{bucket}/file/{filename}")
  @Operation(summary = "get IFC file from minio, create contour and output it as WKT")
  public String createContourFromStorage(
      @Parameter(description = "The name of the source and target MinIO bucket.") @PathVariable String bucket,
      @Parameter(description = "The name of the source file in MinIO bucket.") @PathVariable String filename)
      throws IOException, InterruptedException, InvalidKeyException, ErrorResponseException, InsufficientDataException,
      InternalException, InvalidResponseException, NoSuchAlgorithmException, ServerException, XmlParserException,
      IllegalArgumentException {
    String result = "";

    // copy file from minio into container
    files.getSourceFile(bucket, filename);

    // call IfcContourExtractor with file as input
    String res = execService.callConverter(CPATH + filename);

    // if creation succeeds, give WKT back
    if (res.contains("Successful creation.")) {
      String outputName = filename.split("\\.")[0] + "_ifc_WKT_contour.txt";

      // boolean upload = files.uploadFile(bucket, outputName);
      // if(upload) result = outputName;
      files.uploadFile(bucket, outputName);
      result = outputName;

    } else {
      // if creation fails open log file and print logs
      File dir = new File(CPATH);
      String[] logfile = dir.list((d, s) -> {
        return s.toLowerCase().endsWith(".log");
      });

      BufferedReader reader = new BufferedReader(new FileReader(CPATH + logfile[0]));
      String line;
      while ((line = reader.readLine()) != null) {
        result += line + "\n";
      }

      reader.close();
    }

    // clean folder from old files
    files.removeFiles();

    return result;
  }

}