package com.Microservices.MinIOUploadService.domain.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DTM {

    private String topicality;
    private String positionReferenceSystem;
    private String altitudeReferenceSystem;
    private String projection;
    private String spatialExpansion;
    private String dataStructure;
    private String presentationType;
    private String recordingMethod;
    private String measurementAccuracy;
    private String innerAccuracy;
    private String outerAccuracy;

    public DTM() {

    }


    public DTM(String topicality, String positionReferenceSystem, String altitudeReferenceSystem, String projection, String spatialExpansion, String dataStructure, String presentationType, String recordingMethod, String measurementAccuracy, String innerAccuracy, String outerAccuracy) {
        this.topicality = topicality;
        this.positionReferenceSystem = positionReferenceSystem;
        this.altitudeReferenceSystem = altitudeReferenceSystem;
        this.projection = projection;
        this.spatialExpansion = spatialExpansion;
        this.dataStructure = dataStructure;
        this.presentationType = presentationType;
        this.recordingMethod = recordingMethod;
        this.measurementAccuracy = measurementAccuracy;
        this.innerAccuracy = innerAccuracy;
        this.outerAccuracy = outerAccuracy;
    }
}