package com.Microservices.MinIOUploadService.domain.model;

import java.io.File;

import com.fasterxml.jackson.annotation.JsonProperty;

import org.springframework.web.multipart.MultipartFile;

import lombok.Getter;

@Getter
public class Upload {

    @JsonProperty("DIN SPEC 91391-2")
    private Metadata metadata;
    @JsonProperty("DIN 18740-6")
    private DTM dtm;
    private MultipartFile file;
    private String bucket;

    public Upload(MultipartFile file, String bucket, Metadata metadata, DTM dtm){
        this.file = file;
        this.bucket = bucket;
        this.metadata = metadata;
        this.dtm = dtm;
    }
}
