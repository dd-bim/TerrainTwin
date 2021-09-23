package com.Microservices.SemanticConnector.domain;

public enum Prefix {

    RDF {
        public String toString() {
            return "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>";
        }
    },

    RDFS {
        public String toString() {
            return "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>";
        }
    },

    GEO {
        public String toString() {
            return "PREFIX geo: <http://www.opengis.net/ont/geosparql#>";
        }
    },

    TTO {
        public String toString() {
            return "PREFIX tto: <https://terrain.dd-bim.org/terraintwin/ontology/>";
        }
    },

    XTD {
        public String toString() {
            return "PREFIX xtd: <http://www.iso.org/iso_12006_3_V16#>";
        }
    };

    //public static final String RDF = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>";
    // public static final String RDFS = "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>";
    // public static final String GEO = "PREFIX geo: <http://www.opengis.net/ont/geosparql#>";
    // public static final String TTO = "PREFIX tto: <https://terrain.dd-bim.org/terraintwin/ontology/>";
    // public static final String XTD = "PREFIX xtd: <http://www.iso.org/iso_12006_3_V16#>";
}
