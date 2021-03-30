package com.Microservices.MinIOUploadService.domain.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;

@Getter
public class MetaFile {

    @JsonProperty("DIN SPEC 91391-2")
    private Metadata metadata;
    @JsonProperty("DIN 18740-6")
    private DTM dtm;

    public MetaFile(){

    }

    public MetaFile(Metadata metadata){
        this.metadata = metadata;
    }

    public MetaFile(Metadata metadata, DTM dtm){
        this.metadata = metadata;
        this.dtm = dtm;
    }

}
