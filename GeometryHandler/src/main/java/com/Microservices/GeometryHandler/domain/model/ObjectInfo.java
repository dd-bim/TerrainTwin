
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
     */
    public ObjectInfo(String collectionId, UUID featureId) {
        super();
        this.collectionId = collectionId;
        this.featureId = featureId;
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

}
