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
import java.util.Optional;

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
  @PostMapping(path = {"/createFromFile"}, consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
  @Operation(summary = "Create contour from uploaded IFC file as WKT")
  public ResponseEntity<?> createContourFromFileBase(@RequestParam("file") MultipartFile multipartFile)
      throws IllegalStateException, IOException, InterruptedException {
        return createContourFromFile(multipartFile, null);
  }

    // send file and epsg code to IfcGeometryExtractor and give WKT file back
    @PostMapping(path = {"/createFromFile/{epsg}"}, consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    @Operation(summary = "Create contour from uploaded IFC file as EWKT")
    public ResponseEntity<?> createContourFromFileEpsg(@RequestParam("file") MultipartFile multipartFile, 
    @Parameter(description = "EPSG code") @PathVariable(value = "epsg") Optional<Integer> epsgCode)
        throws IllegalStateException, IOException, InterruptedException {
          return createContourFromFile(multipartFile, epsgCode.get());
    }

  public ResponseEntity<?> createContourFromFile(MultipartFile multipartFile, Integer epsg) throws IllegalStateException, IOException, InterruptedException {
    String results = null;

    // clean folder from old files
    files.removeFiles();

    // copy file into container and get filename
    String filename = files.multipartToFile(multipartFile);

    // call IfcContourExtractor with file as input
    results = execService.callConverter(CPATH + filename, epsg);

    if (results.contains("Successful creation.")) {

      File file = null;
      Path path = null;
      String outputName = null;

      try{
      outputName = filename.split("\\.")[0] + "_ifc_WKT_contour_Slabs.txt";

      // if conversion is successful download ifc file
      file = new File(CPATH + outputName);
      path = Paths.get(CPATH + outputName);
      } catch (Exception e) {
        outputName = filename.split("\\.")[0] + "_ifc_WKT_contour_Walls.txt";

        // if conversion is successful download ifc file
        file = new File(CPATH + outputName);
        path = Paths.get(CPATH + outputName);
      }


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
  @GetMapping(path = {"/createFromStorage/minio/{bucket}/file/{filename}", "/createFromStorage/minio/{bucket}/file/{filename}/{epsg}"})
  @Operation(summary = "get IFC file from minio, create contour and output it as WKT")
  public String createContourFromStorage(
      @Parameter(description = "The name of the source and target MinIO bucket.") @PathVariable String bucket,
      @Parameter(description = "The name of the source file in MinIO bucket.") @PathVariable String filename, 
      @Parameter(description = "EPSG code") @PathVariable(value = "epsg", required = false) Integer epsg)
      throws IOException, InterruptedException, InvalidKeyException, ErrorResponseException, InsufficientDataException,
      InternalException, InvalidResponseException, NoSuchAlgorithmException, ServerException, XmlParserException,
      IllegalArgumentException {
    String result = "";

    // copy file from minio into container
    files.getSourceFile(bucket, filename);

    // call IfcContourExtractor with file as input
    String res = execService.callConverter(CPATH + filename, epsg);

    // if creation succeeds, upload file to MinIO
    if (res.contains("Successful creation.")) {
      String outputName;
      try{
        outputName = filename.split("\\.")[0] + "_ifc_WKT_contour_Slabs.txt";
  
        files.uploadFile(bucket, outputName);
        result = outputName;
        } catch (Exception e) {
          outputName = filename.split("\\.")[0] + "_ifc_WKT_contour_Walls.txt";
  
          files.uploadFile(bucket, outputName);
          result = outputName;
        }

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