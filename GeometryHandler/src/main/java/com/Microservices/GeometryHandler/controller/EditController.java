package com.Microservices.GeometryHandler.controller;

import com.Microservices.GeometryHandler.service.UpdateTIN;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@RefreshScope
@RestController
@Tag(name = "Edit geometries")
@RequestMapping("/geometry/edit")
public class EditController {

  @Autowired
  UpdateTIN update;

  Logger log = LoggerFactory.getLogger(EditController.class);

  // recalculate TIN with added, removed points and breaklines
  @PostMapping("/updateTIN/repo/{repo}")
  @Operation(summary = "Create an updated TIN with update information as JSON")
  @ApiResponse(responseCode = "200", description = "Successful operation")
  public String updateTin(
      @Parameter(description = "The name of the target repository for the import of extra information into GraphDB.") @PathVariable String repo,
      @Parameter(description = "The JSON vor update.") @RequestBody String input) {

        return update.recalculateTIN(input, repo);
  
  }

    // recalculate TIN with added, removed points and breaklines
    @PostMapping("/updateTIN")
    @Operation(summary = "Create an updated TIN with update information as JSON", description = "Only for testing. <br> Information are imported into GraphDB repository bin.")
    @ApiResponse(responseCode = "200", description = "Successful operation")
    public String updateTinTest(@Parameter(description = "The JSON vor update.") @RequestBody String input) {
  
          return update.recalculateTIN(input, "bin");
    
    }
}