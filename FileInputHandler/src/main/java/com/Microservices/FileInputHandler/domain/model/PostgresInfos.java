package com.Microservices.FileInputHandler.domain.model;

import java.util.UUID;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostgresInfos {

    private int originId;
    private UUID id;
    private String version;
    private String url;
    private Integer dimension;
    private Integer coordDimension;
    private String filename;
    private String path;
    private String graphdbRepo;
    private String linkType;
    private UUID linkedGeometry;
    private UUID input;
    private String editor;
    private UUID original;
    private double excavation;
    private double backfill;
    private String description;
    private String timestamp;
    private String phase;

    public PostgresInfos() {
        
    }

    public PostgresInfos(int originId, UUID id, String url, int dimension, int coordDimension, String filename,
            String path, String graphdbRepo) {
        this.originId = originId;
        this.id = id;
        this.version = "1.0";
        this.url = url;
        this.dimension = dimension;
        this.coordDimension = coordDimension;
        this.filename = filename;
        this.path = path;
        this.graphdbRepo = graphdbRepo;
    }

    public PostgresInfos(int originId, UUID id, String url, int dimension, int coordDimension, String filename,
            String path, String graphdbRepo, String linkType, UUID linkedGeometry) {
        this.originId = originId;
        this.id = id;
        this.version = "1.0";
        this.url = url;
        this.dimension = dimension;
        this.coordDimension = coordDimension;
        this.filename = filename;
        this.path = path;
        this.graphdbRepo = graphdbRepo;
        this.linkType = linkType;
        this.linkedGeometry = linkedGeometry;
    }

    public PostgresInfos(UUID id, UUID input, UUID original, String version, String editor, String url, Integer dimension, Integer coordDimension,  String description, String timestamp, String phase, double excavation, double backfill, String graphdbRepo) {
        this.id = id;
        this.input = input;
        this.original = original;
        this.version = version;
        this.editor = editor;
        this.url = url;
        this.dimension = dimension;
        this.coordDimension = coordDimension;
        this.description = description;
        this.timestamp = timestamp;
        this.phase = phase;
        this.excavation = excavation;
        this.backfill = backfill;
        this.graphdbRepo = graphdbRepo;
    }

    public PostgresInfos(UUID id, String url, Integer dimension, Integer coordDimension, String graphdbRepo, String linkType, UUID linkedGeometry) {
        this.id = id;
        this.version = "1.0";
        this.url = url;
        this.dimension = dimension;
        this.coordDimension = coordDimension;
        this.graphdbRepo = graphdbRepo;
        this.linkType = linkType;
        this.linkedGeometry = linkedGeometry;
    }
}
