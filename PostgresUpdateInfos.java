package com.Microservices.GeometryHandler.domain.model;

import java.util.UUID;

public class PostgresUpdateInfos {
    
    private UUID id;
    private UUID input;
    private String version;
    private String editor;
    private String url;
    private Integer dimension;
    private Integer coordDimension;
    private String graphdbRepo;
    private String linkType;
    private UUID linkedGeometry;


    public PostgresUpdateInfos(UUID id, UUID input, String version, String editor, String url, Integer dimension, Integer coordDimension, String graphdbRepo) {
        this.id = id;
        this.input = input;
        this.version = version;
        this.editor = editor;
        this.url = url;
        this.dimension = dimension;
        this.coordDimension = coordDimension;
        this.graphdbRepo = graphdbRepo;
    }

    public PostgresUpdateInfos(UUID id, UUID input, String version, String editor, String url, Integer dimension, Integer coordDimension, String graphdbRepo, String linkType, UUID linkedGeometry) {
        this.id = id;
        this.input = input;
        this.version = version;
        this.editor = editor;
        this.url = url;
        this.dimension = dimension;
        this.coordDimension = coordDimension;
        this.graphdbRepo = graphdbRepo;
        this.linkType = linkType;
        this.linkedGeometry = linkedGeometry;
    }

    public PostgresUpdateInfos(UUID id, String url, Integer dimension, Integer coordDimension, String graphdbRepo, String linkType, UUID linkedGeometry) {
        this.id = id;
        this.url = url;
        this.dimension = dimension;
        this.coordDimension = coordDimension;
        this.graphdbRepo = graphdbRepo;
        this.linkType = linkType;
        this.linkedGeometry = linkedGeometry;
    }


    public UUID getId() {
        return this.id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getInput() {
        return this.input;
    }

    public void setInput(UUID input) {
        this.input = input;
    }

    public String getVersion() {
        return this.version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getEditor() {
        return this.editor;
    }

    public void setEditor(String editor) {
        this.editor = editor;
    }

    public String getUrl() {
        return this.url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Integer getDimension() {
        return this.dimension;
    }

    public void setDimension(Integer dimension) {
        this.dimension = dimension;
    }

    public Integer getCoordDimension() {
        return this.coordDimension;
    }

    public void setCoordDimension(Integer coordDimension) {
        this.coordDimension = coordDimension;
    }

    public String getGraphdbRepo() {
        return this.graphdbRepo;
    }

    public void setGraphdbRepo(String graphdbRepo) {
        this.graphdbRepo = graphdbRepo;
    }

    public String getLinkType() {
        return this.linkType;
    }

    public void setLinkType(String linkType) {
        this.linkType = linkType;
    }

    public UUID getLinkedGeometry() {
        return this.linkedGeometry;
    }

    public void setLinkedGeometry(UUID linkedGeometry) {
        this.linkedGeometry = linkedGeometry;
    }
   
}
