package com.Microservices.GeometryHandler.controller;

import java.util.UUID;

import com.Microservices.GeometryHandler.repositories.TINRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@RefreshScope
@RestController
@Tag(name = "Query Information")
@RequestMapping("/geometry/query")
public class QueryController {

  @Autowired
  TINRepository tinRepo;

  Logger log = LoggerFactory.getLogger(QueryController.class);

  // get folder and use them
  @GetMapping("/exteriorring/tinid/{id}")
  @Operation(summary = "Get the exterior ring of a TIN")
  @ApiResponse(responseCode = "200", description = "Successful operation")
  public String queryTINBoundary(@Parameter(description = "The  id of the TIN.") @PathVariable UUID id) {

    String results = tinRepo.getExteriorRing(id);

    return results;
  }
}