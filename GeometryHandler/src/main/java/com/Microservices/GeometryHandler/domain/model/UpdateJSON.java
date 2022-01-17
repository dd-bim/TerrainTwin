
package com.Microservices.GeometryHandler.domain.model;

import java.util.List;
import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("jsonschema2pojo")
public class UpdateJSON {

    @SerializedName("ObjectInfo")
    @Expose
    private ObjectInfo objectInfo;
    @SerializedName("MetaInfos")
    @Expose
    private MetaInfos metaInfos;
    @SerializedName("Breaklines")
    @Expose
    private List<List<List<Double>>> breaklines = null;
    @SerializedName("AddedPoints")
    @Expose
    private List<List<Double>> addedPoints = null;
    @SerializedName("RemovedPoints")
    @Expose
    private List<List<Double>> removedPoints = null;

    /**
     * No args constructor for use in serialization
     * 
     */
    public UpdateJSON() {
    }

    /**
     * 
     * @param breaklines
     * @param addedPoints
     * @param removedPoints
     * @param metaInfos
     * @param objectInfo
     */
    public UpdateJSON(ObjectInfo objectInfo, MetaInfos metaInfos, List<List<List<Double>>> breaklines, List<List<Double>> addedPoints, List<List<Double>> removedPoints) {
        super();
        this.objectInfo = objectInfo;
        this.metaInfos = metaInfos;
        this.breaklines = breaklines;
        this.addedPoints = addedPoints;
        this.removedPoints = removedPoints;
    }

    public ObjectInfo getObjectInfo() {
        return objectInfo;
    }

    public void setObjectInfo(ObjectInfo objectInfo) {
        this.objectInfo = objectInfo;
    }

    public MetaInfos getMetaInfos() {
        return metaInfos;
    }

    public void setMetaInfos(MetaInfos metaInfos) {
        this.metaInfos = metaInfos;
    }

    public List<List<List<Double>>> getBreaklines() {
        return breaklines;
    }

    public void setBreaklines(List<List<List<Double>>> breaklines) {
        this.breaklines = breaklines;
    }

    public List<List<Double>> getAddedPoints() {
        return addedPoints;
    }

    public void setAddedPoints(List<List<Double>> addedPoints) {
        this.addedPoints = addedPoints;
    }

    public List<List<Double>> getRemovedPoints() {
        return removedPoints;
    }

    public void setRemovedPoints(List<List<Double>> removedPoints) {
        this.removedPoints = removedPoints;
    }

}
