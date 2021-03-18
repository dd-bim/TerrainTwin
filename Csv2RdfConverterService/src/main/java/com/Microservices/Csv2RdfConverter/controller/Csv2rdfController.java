package com.Microservices.Csv2RdfConverter.controller;

import javax.validation.Valid;

import com.Microservices.Csv2RdfConverter.domain.model.ConvertInfos;
import com.Microservices.Csv2RdfConverter.service.Csv2RdfService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@RefreshScope
@RestController
@RequestMapping("/terraintwin")
@CrossOrigin(origins = { "http://localhost:8084", "http://host.docker.internal:7202" })
@Tag(name = "Csv2Rdf Converter", description = "Convert csv files to rdf")
public class Csv2rdfController {

    @Autowired
    Csv2RdfService converter;

    @GetMapping("/csv2rdf/")
    @Operation(summary = "Shows, if service works.")
    @ApiResponse(responseCode = "200", description = "Service works", content = @Content)
    @ApiResponse(responseCode = "404", description = "Service not found", content = @Content)
    public String hello() {
        return "csv2rdf Converter works";
    }

    @PostMapping("/csv2rdf/convert")
    @Operation(summary = "Convert a csv file to a rdf file in turtle sysntax.", description = "Possible value combinations in request body:<br><br> file <br> file, delimiter <br> file, namespace, prefix, superclass <br> all")
    @ApiResponse(responseCode = "200", description = "Conversion performed successfully", content = @Content)
    @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content)
    @ApiResponse(responseCode = "404", description = "Service not found", content = @Content)
    public ResponseEntity<?> createRDF(@Valid @RequestBody ConvertInfos infos) {
        String feedback = converter.convert(infos);
        if (feedback.contains("400")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(feedback);
        } else {
            return ResponseEntity.ok(feedback);
        }
    }
}
