package com.Microservices.GraphDBImportService.domain.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostgresInfos {
    
    private int originId;
    private int id;
    private String url;
    private String type;
    private String filename;
    private String path;
    private String graphdbRepo;

    public PostgresInfos(int originId, int id, String url, String type, String filename, String path, String graphdbRepo) {
        this.originId = originId;
        this.id = id;
        this.url = url;
        this.type = type;
        this.filename = filename;
        this.path = path;
        this.graphdbRepo = graphdbRepo;
    }
}
