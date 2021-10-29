package com.Microservices.FileInputHandler.domain.model;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class Queries {

    @Value("${domain.url}")
    private String domain;
    
    // find a feature of a specific geometry
    public String findFeature(String geometry) {
        return Prefix.GEO.toString() +
        "select ?s where {" +
            "?s geo:hasGeometry <" + geometry + "> . }";   
    }

    //find all classes in a group
    public String getDimension(String geometry) {
        return Prefix.GEO.toString() + 
            "select ?dim where {" + 
                "<" + geometry + "> geo:dimension ?dim ." +
            "}" ;
    }
        
    // find feature, which is bounded by this feature
    public String findBoundedFeature(String feature) {
        return Prefix.TTO.toString() +
        "select * where {" +
            "<" + feature + "> tto:bounds ?o. }";   
    }    

}
