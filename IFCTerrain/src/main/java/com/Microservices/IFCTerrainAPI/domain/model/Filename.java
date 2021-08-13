package com.Microservices.IFCTerrainAPI.domain.model;

public class Filename {
    
    public volatile String filename = "";
    public Filename() {
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }
}
