package com.Microservices.IFCTerrainAPI.domain.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InputConfigs {

  @JsonIgnore
  private String filePath;
  private String fileName;
  private String fileType;
  private String destFileName;
  private String outIFCType;
  private String outFileType;
  @JsonIgnore
  private String logFilePath;
  private String verbosityLevel;
  private boolean calculateTin;
  private boolean recalculateTin;
  private String siteName;
  private String projectName;
  private String editorsFamilyName;
  private String editorsGivenName;
  private String editorsOrganisationName;
  private boolean exportMetadataFile;
  private boolean outIfcPropertySet;
  private boolean exportMetadataDin91391;
  private boolean exportMetadataDin18740;
  private boolean is3D;
  private double minDist;
  private String surfaceType;
  private boolean geoElement;
  private int logeoref;
  private boolean customOrigin;
  private double xOrigin;
  private double yOrigin;
  private double zOrigin;
  private double trueNorth;
  private double scale;
  private String crsName;
  private String crsDescription;
  private String geodeticDatum;
  private String verticalDatum;
  private String projectionName;
  private String projectionZone;
  private String layer;
  private boolean isTin;
  private int horizon;
  private int gridSize;
  private boolean bBox;
  private double bbNorth;
  private double bbEast;
  private double bbSouth;
  private double bbWest;
  private boolean onlyHorizon;
  private String horizonFilter;
  private boolean onlyTypes;
  private boolean ignPos;
  private boolean ignHeight;
  private String breakline_layer;
  private boolean breakline;
  private String host;
  private int port;
  private String user;
  private String password;
  private String database;
  private String schema;
  private String tin_table;
  private String tin_column;
  private String tinid_column;
  private String tin_id;
  private String breakline_table;
  private String breakline_column;
  private String breakline_tin_id;


  public InputConfigs() {
  }


  public InputConfigs(String fileName, String fileType, String destFileName, String outIFCType, String outFileType, String verbosityLevel, boolean calculateTin, boolean recalculateTin, String siteName, String projectName, String editorsFamilyName, String editorsGivenName, String editorsOrganisationName, boolean exportMetadataFile, boolean outIfcPropertySet, boolean exportMetadataDin91391, boolean exportMetadataDin18740, boolean is3D, double minDist, String surfaceType, boolean geoElement, int logeoref, boolean customOrigin, double xOrigin, double yOrigin, double zOrigin, double trueNorth, double scale, String crsName, String crsDescription, String geodeticDatum, String verticalDatum, String projectionName, String projectionZone, String layer, boolean isTin, int horizon, int gridSize, boolean bBox, double bbNorth, double bbEast, double bbSouth, double bbWest, boolean onlyHorizon, String horizonFilter, boolean onlyTypes, boolean ignPos, boolean ignHeight, String breakline_layer, boolean breakline, String host, int port, String user, String password, String database, String schema, String tin_table, String tin_column, String tinid_column, String tin_id, String breakline_table, String breakline_column, String breakline_tin_id) {
    this.filePath = "files/" + fileName;
    this.fileName = fileName;
    this.fileType = fileType;
    this.destFileName = "files/" + destFileName;
    this.outIFCType = outIFCType;
    this.outFileType = outFileType;
    this.logFilePath = "files";
    this.verbosityLevel = verbosityLevel;
    this.calculateTin = calculateTin;
    this.recalculateTin = recalculateTin;
    this.siteName = siteName;
    this.projectName = projectName;
    this.editorsFamilyName = editorsFamilyName;
    this.editorsGivenName = editorsGivenName;
    this.editorsOrganisationName = editorsOrganisationName;
    this.exportMetadataFile = exportMetadataFile;
    this.outIfcPropertySet = outIfcPropertySet;
    this.exportMetadataDin91391 = exportMetadataDin91391;
    this.exportMetadataDin18740 = exportMetadataDin18740;
    this.is3D = is3D;
    this.minDist = minDist;
    this.surfaceType = surfaceType;
    this.geoElement = geoElement;
    this.logeoref = logeoref;
    this.customOrigin = customOrigin;
    this.xOrigin = xOrigin;
    this.yOrigin = yOrigin;
    this.zOrigin = zOrigin;
    this.trueNorth = trueNorth;
    this.scale = scale;
    this.crsName = crsName;
    this.crsDescription = crsDescription;
    this.geodeticDatum = geodeticDatum;
    this.verticalDatum = verticalDatum;
    this.projectionName = projectionName;
    this.projectionZone = projectionZone;
    this.layer = layer;
    this.isTin = isTin;
    this.horizon = horizon;
    this.gridSize = gridSize;
    this.bBox = bBox;
    this.bbNorth = bbNorth;
    this.bbEast = bbEast;
    this.bbSouth = bbSouth;
    this.bbWest = bbWest;
    this.onlyHorizon = onlyHorizon;
    this.horizonFilter = horizonFilter;
    this.onlyTypes = onlyTypes;
    this.ignPos = ignPos;
    this.ignHeight = ignHeight;
    this.breakline_layer = breakline_layer;
    this.breakline = breakline;
    this.host = host;
    this.port = port;
    this.user = user;
    this.password = password;
    this.database = database;
    this.schema = schema;
    this.tin_table = tin_table;
    this.tin_column = tin_column;
    this.tinid_column = tinid_column;
    this.tin_id = tin_id;
    this.breakline_table = breakline_table;
    this.breakline_column = breakline_column;
    this.breakline_tin_id = breakline_tin_id;
  }


  public String getFilePath() {
    return this.filePath;
  }

  public void setFilePath(String filePath) {
    this.filePath = filePath;
  }

  public String getFileName() {
    return this.fileName;
  }

  public void setFileName(String fileName) {
    this.fileName = fileName;
  }

  public String getFileType() {
    return this.fileType;
  }

  public void setFileType(String fileType) {
    this.fileType = fileType;
  }

  public String getDestFileName() {
    return this.destFileName;
  }

  public void setDestFileName(String destFileName) {
    this.destFileName = destFileName;
  }

  public String getOutIFCType() {
    return this.outIFCType;
  }

  public void setOutIFCType(String outIFCType) {
    this.outIFCType = outIFCType;
  }

  public String getOutFileType() {
    return this.outFileType;
  }

  public void setOutFileType(String outFileType) {
    this.outFileType = outFileType;
  }

  public String getLogFilePath() {
    return this.logFilePath;
  }

  public void setLogFilePath(String logFilePath) {
    this.logFilePath = logFilePath;
  }

  public String getVerbosityLevel() {
    return this.verbosityLevel;
  }

  public void setVerbosityLevel(String verbosityLevel) {
    this.verbosityLevel = verbosityLevel;
  }

  public boolean isCalculateTin() {
    return this.calculateTin;
  }

  public boolean getCalculateTin() {
    return this.calculateTin;
  }

  public void setCalculateTin(boolean calculateTin) {
    this.calculateTin = calculateTin;
  }

  public boolean isRecalculateTin() {
    return this.recalculateTin;
  }

  public boolean getRecalculateTin() {
    return this.recalculateTin;
  }

  public void setRecalculateTin(boolean recalculateTin) {
    this.recalculateTin = recalculateTin;
  }

  public String getSiteName() {
    return this.siteName;
  }

  public void setSiteName(String siteName) {
    this.siteName = siteName;
  }

  public String getProjectName() {
    return this.projectName;
  }

  public void setProjectName(String projectName) {
    this.projectName = projectName;
  }

  public String getEditorsFamilyName() {
    return this.editorsFamilyName;
  }

  public void setEditorsFamilyName(String editorsFamilyName) {
    this.editorsFamilyName = editorsFamilyName;
  }

  public String getEditorsGivenName() {
    return this.editorsGivenName;
  }

  public void setEditorsGivenName(String editorsGivenName) {
    this.editorsGivenName = editorsGivenName;
  }

  public String getEditorsOrganisationName() {
    return this.editorsOrganisationName;
  }

  public void setEditorsOrganisationName(String editorsOrganisationName) {
    this.editorsOrganisationName = editorsOrganisationName;
  }

  public boolean isExportMetadataFile() {
    return this.exportMetadataFile;
  }

  public boolean getExportMetadataFile() {
    return this.exportMetadataFile;
  }

  public void setExportMetadataFile(boolean exportMetadataFile) {
    this.exportMetadataFile = exportMetadataFile;
  }

  public boolean isOutIfcPropertySet() {
    return this.outIfcPropertySet;
  }

  public boolean getOutIfcPropertySet() {
    return this.outIfcPropertySet;
  }

  public void setOutIfcPropertySet(boolean outIfcPropertySet) {
    this.outIfcPropertySet = outIfcPropertySet;
  }

  public boolean isExportMetadataDin91391() {
    return this.exportMetadataDin91391;
  }

  public boolean getExportMetadataDin91391() {
    return this.exportMetadataDin91391;
  }

  public void setExportMetadataDin91391(boolean exportMetadataDin91391) {
    this.exportMetadataDin91391 = exportMetadataDin91391;
  }

  public boolean isExportMetadataDin18740() {
    return this.exportMetadataDin18740;
  }

  public boolean getExportMetadataDin18740() {
    return this.exportMetadataDin18740;
  }

  public void setExportMetadataDin18740(boolean exportMetadataDin18740) {
    this.exportMetadataDin18740 = exportMetadataDin18740;
  }

  public boolean isIs3D() {
    return this.is3D;
  }

  public boolean getIs3D() {
    return this.is3D;
  }

  public void setIs3D(boolean is3D) {
    this.is3D = is3D;
  }

  public double getMinDist() {
    return this.minDist;
  }

  public void setMinDist(double minDist) {
    this.minDist = minDist;
  }

  public String getSurfaceType() {
    return this.surfaceType;
  }

  public void setSurfaceType(String surfaceType) {
    this.surfaceType = surfaceType;
  }

  public boolean isGeoElement() {
    return this.geoElement;
  }

  public boolean getGeoElement() {
    return this.geoElement;
  }

  public void setGeoElement(boolean geoElement) {
    this.geoElement = geoElement;
  }

  public int getLogeoref() {
    return this.logeoref;
  }

  public void setLogeoref(int logeoref) {
    this.logeoref = logeoref;
  }

  public boolean isCustomOrigin() {
    return this.customOrigin;
  }

  public boolean getCustomOrigin() {
    return this.customOrigin;
  }

  public void setCustomOrigin(boolean customOrigin) {
    this.customOrigin = customOrigin;
  }

  public double getXOrigin() {
    return this.xOrigin;
  }

  public void setXOrigin(double xOrigin) {
    this.xOrigin = xOrigin;
  }

  public double getYOrigin() {
    return this.yOrigin;
  }

  public void setYOrigin(double yOrigin) {
    this.yOrigin = yOrigin;
  }

  public double getZOrigin() {
    return this.zOrigin;
  }

  public void setZOrigin(double zOrigin) {
    this.zOrigin = zOrigin;
  }

  public double getTrueNorth() {
    return this.trueNorth;
  }

  public void setTrueNorth(double trueNorth) {
    this.trueNorth = trueNorth;
  }

  public double getScale() {
    return this.scale;
  }

  public void setScale(double scale) {
    this.scale = scale;
  }

  public String getCrsName() {
    return this.crsName;
  }

  public void setCrsName(String crsName) {
    this.crsName = crsName;
  }

  public String getCrsDescription() {
    return this.crsDescription;
  }

  public void setCrsDescription(String crsDescription) {
    this.crsDescription = crsDescription;
  }

  public String getGeodeticDatum() {
    return this.geodeticDatum;
  }

  public void setGeodeticDatum(String geodeticDatum) {
    this.geodeticDatum = geodeticDatum;
  }

  public String getVerticalDatum() {
    return this.verticalDatum;
  }

  public void setVerticalDatum(String verticalDatum) {
    this.verticalDatum = verticalDatum;
  }

  public String getProjectionName() {
    return this.projectionName;
  }

  public void setProjectionName(String projectionName) {
    this.projectionName = projectionName;
  }

  public String getProjectionZone() {
    return this.projectionZone;
  }

  public void setProjectionZone(String projectionZone) {
    this.projectionZone = projectionZone;
  }

  public String getLayer() {
    return this.layer;
  }

  public void setLayer(String layer) {
    this.layer = layer;
  }

  public boolean isIsTin() {
    return this.isTin;
  }

  public boolean getIsTin() {
    return this.isTin;
  }

  public void setIsTin(boolean isTin) {
    this.isTin = isTin;
  }

  public int getHorizon() {
    return this.horizon;
  }

  public void setHorizon(int horizon) {
    this.horizon = horizon;
  }

  public int getGridSize() {
    return this.gridSize;
  }

  public void setGridSize(int gridSize) {
    this.gridSize = gridSize;
  }

  public boolean isBBox() {
    return this.bBox;
  }

  public boolean getBBox() {
    return this.bBox;
  }

  public void setBBox(boolean bBox) {
    this.bBox = bBox;
  }

  public double getBbNorth() {
    return this.bbNorth;
  }

  public void setBbNorth(double bbNorth) {
    this.bbNorth = bbNorth;
  }

  public double getBbEast() {
    return this.bbEast;
  }

  public void setBbEast(double bbEast) {
    this.bbEast = bbEast;
  }

  public double getBbSouth() {
    return this.bbSouth;
  }

  public void setBbSouth(double bbSouth) {
    this.bbSouth = bbSouth;
  }

  public double getBbWest() {
    return this.bbWest;
  }

  public void setBbWest(double bbWest) {
    this.bbWest = bbWest;
  }

  public boolean isOnlyHorizon() {
    return this.onlyHorizon;
  }

  public boolean getOnlyHorizon() {
    return this.onlyHorizon;
  }

  public void setOnlyHorizon(boolean onlyHorizon) {
    this.onlyHorizon = onlyHorizon;
  }

  public String getHorizonFilter() {
    return this.horizonFilter;
  }

  public void setHorizonFilter(String horizonFilter) {
    this.horizonFilter = horizonFilter;
  }

  public boolean isOnlyTypes() {
    return this.onlyTypes;
  }

  public boolean getOnlyTypes() {
    return this.onlyTypes;
  }

  public void setOnlyTypes(boolean onlyTypes) {
    this.onlyTypes = onlyTypes;
  }

  public boolean isIgnPos() {
    return this.ignPos;
  }

  public boolean getIgnPos() {
    return this.ignPos;
  }

  public void setIgnPos(boolean ignPos) {
    this.ignPos = ignPos;
  }

  public boolean isIgnHeight() {
    return this.ignHeight;
  }

  public boolean getIgnHeight() {
    return this.ignHeight;
  }

  public void setIgnHeight(boolean ignHeight) {
    this.ignHeight = ignHeight;
  }

  public String getBreakline_layer() {
    return this.breakline_layer;
  }

  public void setBreakline_layer(String breakline_layer) {
    this.breakline_layer = breakline_layer;
  }

  public boolean isBreakline() {
    return this.breakline;
  }

  public boolean getBreakline() {
    return this.breakline;
  }

  public void setBreakline(boolean breakline) {
    this.breakline = breakline;
  }

  public String getHost() {
    return this.host;
  }

  public void setHost(String host) {
    this.host = host;
  }

  public int getPort() {
    return this.port;
  }

  public void setPort(int port) {
    this.port = port;
  }

  public String getUser() {
    return this.user;
  }

  public void setUser(String user) {
    this.user = user;
  }

  public String getPassword() {
    return this.password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getDatabase() {
    return this.database;
  }

  public void setDatabase(String database) {
    this.database = database;
  }

  public String getSchema() {
    return this.schema;
  }

  public void setSchema(String schema) {
    this.schema = schema;
  }

  public String getTin_table() {
    return this.tin_table;
  }

  public void setTin_table(String tin_table) {
    this.tin_table = tin_table;
  }

  public String getTin_column() {
    return this.tin_column;
  }

  public void setTin_column(String tin_column) {
    this.tin_column = tin_column;
  }

  public String getTinid_column() {
    return this.tinid_column;
  }

  public void setTinid_column(String tinid_column) {
    this.tinid_column = tinid_column;
  }

  public String getTin_id() {
    return this.tin_id;
  }

  public void setTin_id(String tin_id) {
    this.tin_id = tin_id;
  }

  public String getBreakline_table() {
    return this.breakline_table;
  }

  public void setBreakline_table(String breakline_table) {
    this.breakline_table = breakline_table;
  }

  public String getBreakline_column() {
    return this.breakline_column;
  }

  public void setBreakline_column(String breakline_column) {
    this.breakline_column = breakline_column;
  }

  public String getBreakline_tin_id() {
    return this.breakline_tin_id;
  }

  public void setBreakline_tin_id(String breakline_tin_id) {
    this.breakline_tin_id = breakline_tin_id;
  }

}

