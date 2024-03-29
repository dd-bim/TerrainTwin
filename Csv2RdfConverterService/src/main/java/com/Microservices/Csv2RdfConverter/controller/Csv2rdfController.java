package com.Microservices.Csv2RdfConverter.controller;

import java.io.File;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import com.Microservices.Csv2RdfConverter.domain.model.ConvertInfos;
import com.Microservices.Csv2RdfConverter.service.Csv2RdfService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
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
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@RefreshScope
@RestController
@Tag(name = "Csv2Rdf Converter", description = "Convert csv files to rdf")
public class Csv2rdfController {

    @Autowired
    Csv2RdfService converter;

    // upload source file for conversion
    @PostMapping(path = "/csv2rdf/convert", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Convert a csv file to a rdf file in turtle sysntax.", description = "Possible value combinations in request body:<br><br> file <br> file, delimiter <br> file, namespace, prefix, superclass <br> all")
    @ApiResponse(responseCode = "200", description = "Conversion performed successfully", content = @Content)
    @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content)
    @ApiResponse(responseCode = "404", description = "Service not found", content = @Content)
    public ResponseEntity<?> createRDF(
            @Parameter(description = "The CSV/TXT source file.") @RequestParam("file") MultipartFile multiFile,
            @Parameter(description = "The name of the target MinIO bucket.") @RequestParam String bucket,
            @Parameter(description = "The delimiter in the source file.") @RequestParam(required = false) String delimiter,
            @Parameter(description = "The namespace for the generated triples.") @RequestParam(required = false) String namespace,
            @Parameter(description = "The prefic for the namespace.") @RequestParam(required = false) String prefix,
            @Parameter(description = "The class to which all generated classes are subordinated.") @RequestParam(required = false) String superclass)
            throws IllegalStateException, IOException, InvalidKeyException, ErrorResponseException,
            InsufficientDataException, InternalException, InvalidResponseException, NoSuchAlgorithmException,
            ServerException, XmlParserException, IllegalArgumentException {
        ConvertInfos infos = new ConvertInfos();
        File file = converter.multipartToFile(multiFile);

        if (delimiter != null && namespace != null && prefix != null && superclass != null) {
            infos = new ConvertInfos(file, namespace, prefix, superclass, delimiter);
        } else if (delimiter == null && namespace == null && prefix == null && superclass == null) {
            infos = new ConvertInfos(file);
        } else if (namespace == null && prefix == null && superclass == null) {
            infos = new ConvertInfos(file, delimiter);
        } else if (delimiter == null) {
            infos = new ConvertInfos(file, namespace, prefix, superclass);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("This is not a valid value combination.");
        }

        String feedback = converter.convert(infos, 0, bucket);
        if (feedback.contains("400")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(feedback);
        } else {
            return ResponseEntity.ok(feedback);
        }
    }

    // get source file for conversion from MinIO bucket
    @GetMapping("/csv2rdf/convert/bucket/{bucket}/file/{filename}")
    @Operation(summary = "Convert a csv file to a rdf file in turtle sysntax.", description = "Possible value combinations in request body:<br><br> file <br> file, delimiter <br> file, namespace, prefix, superclass <br> all")
    public ResponseEntity<?> convertFile(
            @Parameter(description = "The name of the source and target MinIO bucket.") @PathVariable String bucket,
            @Parameter(description = "The name of the source file in MinIO bucket.") @PathVariable String filename,
            @Parameter(description = "The delimiter in the source file.") @RequestParam(required = false) String delimiter,
            @Parameter(description = "The namespace for the generated triples.") @RequestParam(required = false) String namespace,
            @Parameter(description = "The prefic for the namespace.") @RequestParam(required = false) String prefix,
            @Parameter(description = "The class to which all generated classes are subordinated.") @RequestParam(required = false) String superclass)
            throws InvalidKeyException, ErrorResponseException, InsufficientDataException, InternalException,
            InvalidResponseException, NoSuchAlgorithmException, ServerException, XmlParserException,
            IllegalArgumentException {

        File file = converter.getSourceFile(bucket, filename);
        ConvertInfos infos = new ConvertInfos();

        if (delimiter != null && namespace != null && prefix != null && superclass != null) {
            infos = new ConvertInfos(file, namespace, prefix, superclass, delimiter);
        } else if (delimiter == null && namespace == null && prefix == null && superclass == null) {
            infos = new ConvertInfos(file);
        } else if (namespace == null && prefix == null && superclass == null) {
            infos = new ConvertInfos(file, delimiter);
        } else if (delimiter == null) {
            infos = new ConvertInfos(file, namespace, prefix, superclass);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("This is not a valid value combination.");
        }

        String feedback = converter.convert(infos, 0, bucket);
        if (feedback.contains("400")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(feedback);
        } else {
            return ResponseEntity.ok(feedback);
        }
    }
}
