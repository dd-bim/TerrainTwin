package com.Microservices.MinIOUploadService.controller;

import com.Microservices.MinIOUploadService.service.UploadService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@RefreshScope
@RestController
@Tag(name = "Delete", description = "Delete a bucket and his content from MinIO Object storage")
public class DeleteController {

  @Autowired
  UploadService minio;

  @DeleteMapping("/minioupload/deletebucket/{bucket}")
  @Operation(summary = "Delete a bucket")
  @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content)
  public ResponseEntity<?> delete(@Parameter(description = "The name of the old MinIO bucket.") @PathVariable String bucket) throws Exception {
    if (bucket.matches("^[a-z0-9][a-z0-9-.]{1,61}[a-z0-9]$")) {
      
      String results = minio.deleteBucket(bucket);
      return ResponseEntity.ok(results);
    } else {
      ;
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
          "The bucket name is not valid. The name must be at least 3 characters long, start and end with a digit or lowercase letter and contain only 'a-z', '0-9', '.' and '-''. More than one dot or hyphen in a row is also not allowed.");
    }
  }

}