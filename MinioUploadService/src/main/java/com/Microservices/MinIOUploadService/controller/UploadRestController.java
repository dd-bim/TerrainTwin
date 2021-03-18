package com.Microservices.MinIOUploadService.controller;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.validation.Valid;

import com.Microservices.MinIOUploadService.domain.model.UploadInfos;
import com.Microservices.MinIOUploadService.service.UploadService;

import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import io.minio.errors.ErrorResponseException;
import io.minio.errors.InsufficientDataException;
import io.minio.errors.InternalException;
import io.minio.errors.InvalidResponseException;
import io.minio.errors.ServerException;
import io.minio.errors.XmlParserException;

// import io.swagger.v3.oas.annotations.ExternalDocumentation;
// import io.swagger.v3.oas.annotations.tags.Tag;

@RefreshScope
@RestController
// @RequestMapping("/terraintwin")
@CrossOrigin(origins = "http://localhost:8084")
// @Tag(name = "GraphDBImporter", description = "Import semantic data from MinIo
// Object Storage into a GraphDB database", externalDocs =
// @ExternalDocumentation(url = "/terraintwin/graphdbimport/home", description =
// "Interface"))
public class UploadRestController {

  @GetMapping("/")
  public String index() {
    return "hello world";
  }

  @GetMapping("/minioupload/miniobucket/{bucket}")
  public String send(@PathVariable String bucket) throws Exception {
    UploadService minio = new UploadService();
    String results = minio.createBucket(bucket);

    return results;
  }

  @PostMapping("/minioupload")
  // @Operation(summary = "Convert a csv file to a rdf file in turtle sysntax.",
  // description = "Possible value combinations in request body:<br><br> file <br>
  // file, delimiter <br> file, namespace, prefix, superclass <br> all")
  // @ApiResponse(responseCode = "200", description = "Conversion performed
  // successfully", content = @Content)
  // @ApiResponse(responseCode = "400", description = "Bad Request", content =
  // @Content)
  // @ApiResponse(responseCode = "404", description = "Service not found", content
  // = @Content)
  // ResponseEntity<?>
  public String uploadFile(@Valid @RequestBody UploadInfos infos) throws InvalidKeyException, ErrorResponseException,
      InsufficientDataException, InternalException, InvalidResponseException, NoSuchAlgorithmException, ServerException,
      XmlParserException, IllegalArgumentException, IOException {
    UploadService minio = new UploadService();
    String results = minio.upload(infos);

    return results;
    // if (feedback.contains("400")) {
    // return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(feedback);
    // } else {
    // return ResponseEntity.ok(feedback);
    // }
  }
}