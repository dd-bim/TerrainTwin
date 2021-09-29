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
  private String siteName;
  private String projectName;
  private String editorsFamilyName;
  private String editorsGivenName;
  private String editorsOrganisationName;
  private boolean outIfcPropertySet;
  private boolean exportMetadataDin91391;
  private boolean exportMetadataDin18740;
  private String outSurfaceType;
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
  private int horizon;
  private boolean bBox;
  private double bbP1X;
  private double bbP1Y;
  private double bbP2X;
  private double bbP2Y;
  private boolean onlyHorizon;
  private String horizonFilter;
  private String breakline_layer;
  private boolean breakline;
  private String host;
  private int port;
  private String user;
  private String pwd;
  private String database;
  private String schema;
  private String tin_table;
  private String tin_column;
  private String tinid_column;
  private String tin_id;
  private String breakline_table;
  private String breakline_column;
  private String breakline_tin_id;
  private boolean readPoints;
  private boolean invertedCRS;
  private boolean filterPoints;
  private String queryString;
  private String breaklineQueryString;
  private String geometryType;

  public InputConfigs() {
  }

  public InputConfigs(String fileName, String fileType, String destFileName, String outIFCType, String outFileType,
      String verbosityLevel, String siteName, String projectName, String editorsFamilyName, String editorsGivenName,
      String editorsOrganisationName, boolean outIfcPropertySet, boolean exportMetadataDin91391,
      boolean exportMetadataDin18740, String outSurfaceType, boolean geoElement, int logeoref,
      boolean customOrigin, double xOrigin, double yOrigin, double zOrigin, double trueNorth, double scale,
      String crsName, String crsDescription, String geodeticDatum, String verticalDatum, String projectionName,
      String projectionZone, String layer, int horizon, boolean bBox, double bbP1X, double bbP1Y, double bbP2X,
      double bbP2Y, boolean onlyHorizon, String horizonFilter, String breakline_layer, boolean breakline, String host,
      int port, String user, String pwd, String database, String schema, String tin_table, String tin_column,
      String tinid_column, String tin_id, String breakline_table, String breakline_column, String breakline_tin_id,
      boolean readPoints, boolean invertedCRS, boolean filterPoints, String queryString, String breaklineQueryString,
      String geometryType) {
    this.filePath = "files/" + fileName;
    this.fileName = fileName;
    this.fileType = fileType;
    this.destFileName = "files/" + destFileName;
    this.outIFCType = outIFCType;
    this.outFileType = outFileType;
    this.logFilePath = "files";
    this.verbosityLevel = verbosityLevel;
    this.siteName = siteName;
    this.projectName = projectName;
    this.editorsFamilyName = editorsFamilyName;
    this.editorsGivenName = editorsGivenName;
    this.editorsOrganisationName = editorsOrganisationName;
    this.outIfcPropertySet = outIfcPropertySet;
    this.exportMetadataDin91391 = exportMetadataDin91391;
    this.exportMetadataDin18740 = exportMetadataDin18740;
    this.outSurfaceType = outSurfaceType;
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
    this.horizon = horizon;
    this.bBox = bBox;
    this.bbP1X = bbP1X;
    this.bbP1Y = bbP1Y;
    this.bbP2X = bbP2X;
    this.bbP2Y = bbP2Y;
    this.onlyHorizon = onlyHorizon;
    this.horizonFilter = horizonFilter;
    this.breakline_layer = breakline_layer;
    this.breakline = breakline;
    this.host = host;
    this.port = port;
    this.user = user;
    this.pwd = pwd;
    this.database = database;
    this.schema = schema;
    this.tin_table = tin_table;
    this.tin_column = tin_column;
    this.tinid_column = tinid_column;
    this.tin_id = tin_id;
    this.breakline_table = breakline_table;
    this.breakline_column = breakline_column;
    this.breakline_tin_id = breakline_tin_id;
    this.readPoints = readPoints;
    this.invertedCRS = invertedCRS;
    this.filterPoints = filterPoints;
    this.queryString = queryString;
    this.breaklineQueryString = breaklineQueryString;
    this.geometryType = geometryType;
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

  public String getOutSurfaceType() {
    return this.outSurfaceType;
  }

  public void setOutSurfaceType(String outSurfaceType) {
    this.outSurfaceType = outSurfaceType;
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

  public int getHorizon() {
    return this.horizon;
  }

  public void setHorizon(int horizon) {
    this.horizon = horizon;
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

  public double getBbP1X() {
    return this.bbP1X;
  }

  public void setBbP1X(double bbP1X) {
    this.bbP1X = bbP1X;
  }

  public double getBbP1Y() {
    return this.bbP1Y;
  }

  public void setBbP1Y(double bbP1Y) {
    this.bbP1Y = bbP1Y;
  }

  public double getBbP2X() {
    return this.bbP2X;
  }

  public void setBbP2X(double bbP2X) {
    this.bbP2X = bbP2X;
  }

  public double getBbP2Y() {
    return this.bbP2Y;
  }

  public void setBbP2Y(double bbP2Y) {
    this.bbP2Y = bbP2Y;
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

  public String getPwd() {
    return this.pwd;
  }

  public void setPwd(String pwd) {
    this.pwd = pwd;
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

  public boolean isReadPoints() {
    return this.readPoints;
  }

  public boolean getReadPoints() {
    return this.readPoints;
  }

  public void setReadPoints(boolean readPoints) {
    this.readPoints = readPoints;
  }

  public boolean isInvertedCRS() {
    return this.invertedCRS;
  }

  public boolean getInvertedCRS() {
    return this.invertedCRS;
  }

  public void setInvertedCRS(boolean invertedCRS) {
    this.invertedCRS = invertedCRS;
  }

  public boolean isFilterPoints() {
    return this.filterPoints;
  }

  public boolean getFilterPoints() {
    return this.filterPoints;
  }

  public void setFilterPoints(boolean filterPoints) {
    this.filterPoints = filterPoints;
  }

  public String getQueryString() {
    return this.queryString;
  }

  public void setQueryString(String queryString) {
    this.queryString = queryString;
  }

  public String getBreaklineQueryString() {
    return this.breaklineQueryString;
  }

  public void setBreaklineQueryString(String breaklineQueryString) {
    this.breaklineQueryString = breaklineQueryString;
  }

  public String getGeometryType() {
    return this.geometryType;
  }

  public void setGeometryType(String geometryType) {
    this.geometryType = geometryType;
  }

  public String getSiteName() {
    return this.siteName;
  }

  public void setSiteName(String siteName) {
    this.siteName = siteName;
  }

}