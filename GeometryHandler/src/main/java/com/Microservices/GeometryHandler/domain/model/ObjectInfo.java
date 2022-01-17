
package com.Microservices.GeometryHandler.domain.model;

import java.util.UUID;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("jsonschema2pojo")
public class ObjectInfo {

    @SerializedName("CollectionId")
    @Expose
    private String collectionId;
    @SerializedName("FeatureId")
    @Expose
    private UUID featureId;
    @SerializedName("SRID")
    @Expose
    private Integer srid;

    /**
     * No args constructor for use in serialization
     * 
     */
    public ObjectInfo() {
    }

    /**
     * 
     * @param collectionId
     * @param featureId
     * @param srid
     */
    public ObjectInfo(String collectionId, UUID featureId, Integer srid) {
        super();
        this.collectionId = collectionId;
        this.featureId = featureId;
        this.srid = srid;
    }

    public String getCollectionId() {
        return collectionId;
    }

    public void setCollectionId(String collectionId) {
        this.collectionId = collectionId;
    }

    public UUID getFeatureId() {
        return featureId;
    }

    public void setFeatureId(UUID featureId) {
        this.featureId = featureId;
    }

    public Integer getSrid() {
        return srid;
    }

    public void setSrid(Integer srid) {
        this.srid = srid;
    }

}
