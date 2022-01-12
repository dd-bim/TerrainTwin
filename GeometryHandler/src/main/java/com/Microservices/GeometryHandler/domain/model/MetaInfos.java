
package com.Microservices.GeometryHandler.domain.model;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("jsonschema2pojo")
public class MetaInfos {

    @SerializedName("GroundChange")
    @Expose
    private Double groundChange;
    @SerializedName("User")
    @Expose
    private String user;
    @SerializedName("SRID")
    @Expose
    private Integer srid;

    /**
     * No args constructor for use in serialization
     * 
     */
    public MetaInfos() {
    }

    /**
     * 
     * @param groundChange
     * @param user
     * @param srid
     */
    public MetaInfos(Double groundChange, String user, Integer srid) {
        super();
        this.groundChange = groundChange;
        this.user = user;
        this.srid = srid;
    }

    public Double getGroundChange() {
        return groundChange;
    }

    public void setGroundChange(Double groundChange) {
        this.groundChange = groundChange;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public Integer getSrid() {
        return srid;
    }

    public void setSrid(Integer srid) {
        this.srid = srid;
    }

}
