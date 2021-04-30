package com.Microservices.Csv2RdfConverter.domain.model;

import java.io.File;

import javax.validation.constraints.NotBlank;

import lombok.Getter;

@Getter
public class ConvertInfos {

    @NotBlank
    private File file;
    private String namespace;
    private String prefix;
    private String superclass;
    private String delimiter;

    public ConvertInfos(File file, String namespace, String prefix, String superclass, String delimiter) {
        this.file = file;
        this.namespace = namespace;
        this.prefix = prefix;
        this.superclass = superclass;
        this.delimiter = delimiter;
    }

    public ConvertInfos() {

    }

    public ConvertInfos(File file) {
        this.file = file;
    }

    public ConvertInfos(File file, String delimiter) {
        this.file = file;
        this.delimiter = delimiter;
    }

    public ConvertInfos(File file, String namespace, String prefix, String superclass) {
        this.file = file;
        this.namespace = namespace;
        this.prefix = prefix;
        this.superclass = superclass;
    }
}
