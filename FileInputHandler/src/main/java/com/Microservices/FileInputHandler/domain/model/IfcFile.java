package com.Microservices.FileInputHandler.domain.model;

import java.io.File;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class IfcFile {
    private File file;
    private String version;


    public IfcFile() {
    }

    public IfcFile(File file, String version) {
        this.file = file;
        this.version = version;
    }

}
