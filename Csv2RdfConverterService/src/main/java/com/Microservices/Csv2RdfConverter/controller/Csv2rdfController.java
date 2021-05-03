package com.Microservices.Csv2RdfConverter.controller;

import java.io.File;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import com.Microservices.Csv2RdfConverter.domain.model.ConvertInfos;
import com.Microservices.Csv2RdfConverter.service.Csv2RdfService;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
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
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@RefreshScope
@RestController
@Tag(name = "Csv2Rdf Converter", description = "Convert csv files to rdf")
public class Csv2rdfController {

    // @Autowired
    // Csv2RdfService converter;

    @Value("${minio.url}")
    private String url;
    @Value("${minio.port}")
    private String port;
    @Value("${minio.access_key}")
    private String access_key;
    @Value("${minio.secret_key}")
    private String secret_key;

    @GetMapping("/csv2rdf/")
    @Operation(summary = "Shows, if service works.")
    @ApiResponse(responseCode = "200", description = "Service works", content = @Content)
    @ApiResponse(responseCode = "404", description = "Service not found", content = @Content)
    public String hello() {
        return "csv2rdf Converter works";
    }

    @PostMapping(path = "/csv2rdf/convert", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Convert a csv file to a rdf file in turtle sysntax.", description = "Possible value combinations in request body:<br><br> file <br> file, delimiter <br> file, namespace, prefix, superclass <br> all")
    @ApiResponse(responseCode = "200", description = "Conversion performed successfully", content = @Content)
    @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content)
    @ApiResponse(responseCode = "404", description = "Service not found", content = @Content)
    public ResponseEntity<?> createRDF(@RequestParam("file") MultipartFile multiFile, @RequestParam String bucket,
            @RequestParam(required = false) String delimiter, @RequestParam(required = false) String namespace,
            @RequestParam(required = false) String prefix, @RequestParam(required = false) String superclass)
            throws IllegalStateException, IOException, InvalidKeyException, ErrorResponseException, InsufficientDataException, InternalException, InvalidResponseException, NoSuchAlgorithmException, ServerException, XmlParserException, IllegalArgumentException {
        ConvertInfos infos = new ConvertInfos();
        Csv2RdfService converter = new Csv2RdfService(url, port, access_key, secret_key);
        File file = converter.multipartToFile(multiFile);
        String f = multiFile.getOriginalFilename();
        System.out.println(f);
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
