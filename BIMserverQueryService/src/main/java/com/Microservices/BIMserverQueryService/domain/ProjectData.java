package com.Microservices.BIMserverQueryService.domain;

public class ProjectData {
    
    private String schema;
    private long roid;
    private long serializerOid;


    public ProjectData() {
    }

    public ProjectData(String schema, long roid, long serializerOid) {
        this.schema = schema;
        this.roid = roid;
        this.serializerOid = serializerOid;
    }

    public String getSchema() {
        return this.schema;
    }

    public void setSchema(String schema) {
        this.schema = schema;
    }

    public long getRoid() {
        return this.roid;
    }

    public void setRoid(long roid) {
        this.roid = roid;
    }

    public long getSerializerOid() {
        return this.serializerOid;
    }

    public void setSerializerOid(long serializerOid) {
        this.serializerOid = serializerOid;
    }

}
