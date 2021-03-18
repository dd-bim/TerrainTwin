package com.Microservices.MinIOUploadService.domain.model;

import java.io.File;

import javax.validation.constraints.NotBlank;

import lombok.Getter;

@Getter
public class UploadInfos {

    @NotBlank
    private String bucket;
    private File file;
    private String path;

    public UploadInfos(String bucket, String path) {
        this.bucket = bucket;
        this.path = path;
    }

    public UploadInfos(String bucket, File file) {
        this.bucket = bucket;
        this.file = file;
        this.path = file.getAbsolutePath();
    }

    public UploadInfos() {

    }
}
