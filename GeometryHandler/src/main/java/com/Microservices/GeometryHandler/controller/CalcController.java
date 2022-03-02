package com.Microservices.GeometryHandler.controller;

import java.util.Optional;
import java.util.UUID;

import com.Microservices.GeometryHandler.domain.model.GradientInfos;
import com.Microservices.GeometryHandler.domain.model.Volume;
import com.Microservices.GeometryHandler.schemas.Breaklines;
import com.Microservices.GeometryHandler.schemas.TIN;
import com.Microservices.GeometryHandler.service.GradientCalculator;
import com.Microservices.GeometryHandler.service.VolumeCalculation;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import org.locationtech.jts.geom.GeometryCollection;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.LinearRing;
import org.locationtech.jts.geom.PrecisionModel;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKTReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@RefreshScope
@RestController
@Tag(name = "Calculate with geometry")
@RequestMapping("/geometry/calc")
public class CalcController {

  @Autowired
  VolumeCalculation getVolume;

  @Autowired
  GradientCalculator getGradient;

  @Autowired
  ExportController export;

  Logger log = LoggerFactory.getLogger(CalcController.class);

  // calculate mass change between two TINs
  @PostMapping("/massDifference/firstTIN/{firstTinId}/secondTIN/{secondTinId}")
  @Operation(summary = "Calculate the mass change between two TINs", description = "Beside the mass change of the whole TINs it also can be calculated with a breakline boundary or a arbitrary boundary")
  @ApiResponse(responseCode = "200", description = "Successful operation")
  public String getMassDifference(
      @Parameter(description = "\"Upper\" TIN id for comparison") @PathVariable UUID firstTinId,
      @Parameter(description = "\"Lower\" TIN id for comparison") @PathVariable UUID secondTinId,
      @Parameter(description = "Boundary for a restricted mass calculation, featureId for a breakline from PostgresDB or a closed WKT LineString") @RequestParam Optional<String> boundary)
      throws ParseException {
    PrecisionModel precisionModel = new PrecisionModel(1000);
    Gson gson = new GsonBuilder().setPrettyPrinting().create();

    TIN fTin = TIN.class.cast(export.getFeaturePage("application/json", "dtm_tin", firstTinId));
    String[] fGeom = fTin.getGeometry().split(";");
    int epsg = Integer.parseInt(fGeom[0].replace("SRID=", ""));
    String fGeometry = fGeom[1];
    fGeometry = "GEOMETRYCOLLECTION Z(" + fGeometry.replace("TIN(", "").replaceAll("\\(\\(", "POLYGON Z((");

    TIN sTin = TIN.class.cast(export.getFeaturePage("application/json", "dtm_tin", secondTinId));
    String[] sGeom = sTin.getGeometry().split(";");
    String sGeometry = sGeom[1];
    sGeometry = "GEOMETRYCOLLECTION Z(" + sGeometry.replace("TIN(", "").replaceAll("\\(\\(", "POLYGON Z((");

    GeometryFactory factory = new GeometryFactory(precisionModel, epsg); // both tins have same epsg code
    WKTReader wkt = new WKTReader(factory);

    GeometryCollection firstTin = (GeometryCollection) wkt.read(fGeometry);
    GeometryCollection secondTin = (GeometryCollection) wkt.read(sGeometry);

    LinearRing ring = null;
    if (boundary.isPresent()) {
      String bGeometry = boundary.get();
      try {
        UUID id = UUID.fromString(bGeometry);
        Breaklines bl = Breaklines.class.cast(export.getFeaturePage("application/json", "dtm_breaklines", id));
        String[] bGeom = bl.getGeometry().split(";");
        bGeometry = bGeom[1];
      } catch (IllegalArgumentException e) {
        log.info("No uuid");
      }
      LineString ls = (LineString) wkt.read(bGeometry);
      ring = new LinearRing(ls.getCoordinateSequence(), factory);
    }
    Volume volume = getVolume.calculateVolume(firstTin, secondTin, ring, 25832);
    JsonObject resultJson = new JsonObject();
    resultJson.addProperty("Mass change [cbm]", volume.Positive - volume.Negative);
    resultJson.addProperty("Excavation [cbm]", volume.Negative);
    resultJson.addProperty("Backfill [cbm]", volume.Positive);

    return gson.toJson(resultJson);
  }

  // calculate mass change between two TINs
  @PostMapping("/gradient")
  @Operation(summary = "Calculate the gradients in a radius around a point")
  @ApiResponse(responseCode = "200", description = "Successful operation")
  public String getGradients(
      @Parameter(description = "TIN id") @RequestParam UUID tinId,
      @Parameter(description = "X value of point") @RequestParam double x,
      @Parameter(description = "Y value of point") @RequestParam double y,
      @Parameter(description = "Radius around the point") @RequestParam double radius) throws ParseException {
    PrecisionModel precisionModel = new PrecisionModel(1000);
    Gson gson = new GsonBuilder().setPrettyPrinting().create();

    TIN tin = TIN.class.cast(export.getFeaturePage("application/json", "dtm_tin", tinId));
    String[] geom = tin.getGeometry().split(";");
    int epsg = Integer.parseInt(geom[0].replace("SRID=", ""));
    String geometry = geom[1];
    geometry = "GEOMETRYCOLLECTION Z(" + geometry.replace("TIN(", "").replaceAll("\\(\\(", "POLYGON Z((");

    GeometryFactory factory = new GeometryFactory(precisionModel, epsg); // both tins have same epsg code
    WKTReader wkt = new WKTReader(factory);

    GeometryCollection coll = (GeometryCollection) wkt.read(geometry);

    GradientInfos infos = getGradient.getGradientInfos(coll, new double[] { x, y }, radius);

    JsonObject resultJson = new JsonObject();
    resultJson.addProperty("Number of faces", infos.getFaceNumber());
    resultJson.addProperty("Minimum gradient [%]", infos.getMin());
    resultJson.addProperty("Maximum gradient [%]", infos.getMax());
    resultJson.addProperty("Average gradient [%]", infos.getAverage());
    resultJson.addProperty("Median gradient [%]", infos.getMedian());

    return gson.toJson(resultJson);
  }

}