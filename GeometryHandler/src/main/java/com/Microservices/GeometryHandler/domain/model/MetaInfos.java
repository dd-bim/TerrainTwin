
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
    @SerializedName("Description")
    @Expose
    private String description;
    @SerializedName("Timestamp")
    @Expose
    private String timestamp;
    @SerializedName("Phase")
    @Expose
    private String phase;

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
     * @param description
     * @param timestamp
     * @param phase
     */
    public MetaInfos(Double groundChange, String user, String description, String timestamp, String phase) {
        super();
        this.groundChange = groundChange;
        this.user = user;
        this.description = description;
        this.timestamp = timestamp;
        this.phase = phase;
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

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTimestamp() {
        return this.timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getPhase() {
        return this.phase;
    }

    public void setPhase(String phase) {
        this.phase = phase;
    }
}
