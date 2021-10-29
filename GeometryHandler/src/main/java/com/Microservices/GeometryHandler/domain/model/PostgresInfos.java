package com.Microservices.GeometryHandler.domain.model;

import java.util.UUID;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostgresInfos {

    private int originId;
    private UUID id;
    private String url;
    private Integer dimension;
    private Integer coordDimension;
    private String filename;
    private String path;
    private String graphdbRepo;
    private UUID bounds;

    public PostgresInfos() {
        
    }

    public PostgresInfos(int originId, UUID id, String url, int dimension, int coordDimension, String filename,
            String path, String graphdbRepo) {
        this.originId = originId;
        this.id = id;
        this.url = url;
        this.dimension = dimension;
        this.coordDimension = coordDimension;
        this.filename = filename;
        this.path = path;
        this.graphdbRepo = graphdbRepo;
        this.bounds = null;
    }

    public PostgresInfos(int originId, UUID id, String url, int dimension, int coordDimension, String filename,
            String path, String graphdbRepo, UUID bounds) {
        this.originId = originId;
        this.id = id;
        this.url = url;
        this.dimension = dimension;
        this.coordDimension = coordDimension;
        this.filename = filename;
        this.path = path;
        this.graphdbRepo = graphdbRepo;
        this.bounds = bounds;
    }
}
