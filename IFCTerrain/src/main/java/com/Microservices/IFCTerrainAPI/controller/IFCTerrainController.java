package com.Microservices.IFCTerrainAPI.controller;

import java.io.IOException;

import com.Microservices.IFCTerrainAPI.domain.model.InputConfigs;
import com.Microservices.IFCTerrainAPI.service.ExecService;

import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

@RefreshScope
@RestController
@Tag(name = "IFCTerrain Converter")
public class IFCTerrainController {

  @PostMapping("/ifcterrain/converter")
  @ApiResponse(responseCode = "200", description = "Successful operation")
  public String helloService(@RequestBody InputConfigs config) throws IOException, InterruptedException {
    ExecService service = new ExecService();
      return service.callConverter(config);
  }


}