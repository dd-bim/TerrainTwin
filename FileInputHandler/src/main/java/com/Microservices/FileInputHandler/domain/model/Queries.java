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
                "?s geo:hasGeometry postgres:" + geometry + " . }";
    }

    // find dimension of a geometry
    public String getDimension(String geometry) {
        return "PREFIX postgres: <" + domain + "/postgres/>" +
                Prefix.GEO.toString() +
                "select ?dim where {" +
                "postgres:" + geometry + " geo:dimension ?dim . }";
    }

    // find feature, which is bounded by this feature
    public String findBoundedFeature(String feature) {
        return Prefix.TTO.toString() +
                "select * where {" +
                "<" + feature + "> tto:bounds ?o. }";
    }

    // find general information for geometry
    public String getGeomInfos(String geometry) {
        return "PREFIX postgres: <" + domain + "/postgres/>" +
                "select * where {" +
                "postgres:" + geometry + "?p ?o . }";
    }

    // get breaklines of TIN
    public String getBreaklinesFromGeom(String geometry) {
        return "PREFIX postgres: <" + domain + "/postgres/>" +
                Prefix.TTO.toString() +
                "select * where {" +
                "?s tto:breaklineOf postgres:" + geometry + " . }";
    }

    // get connection from TIN Updates
    public String getTINUpdates(String geometry) {
        return Prefix.RDF.toString() +
        Prefix.TTO.toString() +
        "PREFIX postgres: <" + domain + "/postgres/>" +
        "select ?in ?out ?orig where {" +
            "?s rdf:type tto:TINUpdate ." +
                "?s tto:input ?in ." +
                "?s tto:output ?out ." +
                "?s tto:original ?orig ." +
            "filter(postgres:" + geometry + " in (?in) ||  postgres:" + geometry + " in (?out) || postgres:" + geometry + " in (?orig)) }";
        }

    // get version and original tin id for TINUpdate
    public String getInfosForTINUpdate(String geometry) {
        return "PREFIX postgres: <" + domain + "/postgres/>" +
                Prefix.TTO.toString() +
                Prefix.GEO.toString() +
                "select ?version ?source ?original where {" + 
                    "postgres:" + geometry + " tto:geoVersion ?version ." +
                    "optional {" +
                        "?feature geo:hasGeometry postgres:" + geometry + " ." + 
                        "?feature tto:hasSource ?source ." +
                    "}" + 
                    "optional { " +
                        "?t tto:output postgres:" + geometry + " . " +
                        "?t tto:original ?original . " +
                        "?ofeature geo:hasGeometry ?original ." +
                        "?ofeature tto:hasSource ?source ." +
                    "}}"; 
    }

    // get source document of geometry
    public String getSourceFile(String geometry) {
        return "PREFIX postgres: <" + domain + "/postgres/>" +
                Prefix.TTO.toString() +
                Prefix.GEO.toString() +
                "select ?doc where {" + 
                    "?f geo:hasGeometry postgres:" + geometry + " ." +
                    "?f tto:hasSource ?doc . }";  
    }

}
