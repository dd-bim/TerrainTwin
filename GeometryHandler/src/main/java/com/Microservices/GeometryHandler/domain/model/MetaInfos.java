
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
     */
    public MetaInfos(Double groundChange, String user) {
        super();
        this.groundChange = groundChange;
        this.user = user;
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

}
