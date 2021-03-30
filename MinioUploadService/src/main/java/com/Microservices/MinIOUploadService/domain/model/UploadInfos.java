package com.Microservices.MinIOUploadService.domain.model;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.validation.constraints.NotBlank;

import lombok.Getter;

@Getter
public class UploadInfos {

    @NotBlank
    private String bucket;
    private File file;
    private String path;
    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss"); 
    private String timestamp;

    public UploadInfos(String bucket, String path) {
        this.bucket = bucket;
        this.path = path;
        this.timestamp = format.format(new Date()).replaceAll(":", "-");
    }

    public UploadInfos(String bucket, File file) {
        this.bucket = bucket;
        this.file = file;
        this.path = file.getAbsolutePath();
        this.timestamp = format.format(new Date()).replaceAll(":", "-");
    }

    public UploadInfos() {

    }
}
