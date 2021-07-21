package com.Microservices.IFCTerrainAPI.controller;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import com.Microservices.IFCTerrainAPI.domain.model.InputConfigs;
import com.Microservices.IFCTerrainAPI.service.ExecService;
import com.Microservices.IFCTerrainAPI.service.ProcessFiles;

import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;

import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import io.minio.errors.ErrorResponseException;
import io.minio.errors.InsufficientDataException;
import io.minio.errors.InternalException;
import io.minio.errors.InvalidResponseException;
import io.minio.errors.ServerException;
import io.minio.errors.XmlParserException;

@RefreshScope
@RestController
@Tag(name = "IFCTerrain Converter")
public class IFCTerrainController {

  @Autowired
  ProcessFiles files;

  @Autowired
  ExecService execService;

  @PostMapping("/ifcterrain/converter/bucket/{bucket}")
  @ApiResponse(responseCode = "200", description = "Successful operation")
  public String convertToIFC(@PathVariable String bucket, @RequestBody InputConfigs config)
      throws IOException, InterruptedException, InvalidKeyException, ErrorResponseException, InsufficientDataException,
      InternalException, InvalidResponseException, NoSuchAlgorithmException, ServerException, XmlParserException,
      IllegalArgumentException {

    String result = "";

    // set docker internal paths
    config.setFilePath("files/" + config.getFileName());
    config.setDestFileName("files/" + config.getDestFileName());
    config.setLogFilePath("files");

    // check if data comes from a source file and has a convertable type


    String type = config.getFileType().toUpperCase();
    if (type.equals("LANDXML") || type.equals("1"))
      type = "XML";
    if (type.equals("0"))
      type = "DXF";
    if (type.equals("2") || type.equals("CITYGML")) type = "GML";
    if (type.equals("3")) type = "OUT";
    if (type.equals("6")) type = "REB";

    // if (type.equals("DXF") || type.equals("XML")) {

      
      if (config.getFileName() != null)  {

        // copy source file into container if exists
        result = files.getFileFromMinIO(bucket, config.getFileName());

      } else if (config.getHost() != null) {
        // should only be, if data come from PostGIS
        result = "OK";
      } else {
        result = "Problem";
      }

      // check if copy was successful
      if (result.equals("OK")) {

        // create file from configs
        String configFile = files.createConfigFile(config);

        // call converter with config file as input
        result = execService.callConverter(configFile);

        // upload all created files to MinIO bucket
        // if conversion fails, log and configs file will be uploaded
        result += files.upload(bucket, type);

        // delete all created files in container
        files.removeFiles();
      }
    // }

  return result;
}

}