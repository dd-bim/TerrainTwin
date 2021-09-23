package com.Microservices.SemanticConnector.domain;

public enum StaticQuery {
    
    // get all geo:feature instances 
    FEATURES {

        public String toString() {
            return Prefix.RDF.toString() +
            Prefix.GEO.toString() + 
            "select ?s where {" + 
                "?s rdf:type geo:Feature ." +
            "}" ;
        }
    },

    // get all TerrainTwinObject subclasses from tto ontology
    TTOBJCLASSES {

        public String toString() {
            return Prefix.RDFS.toString() +
            Prefix.TTO.toString() + 
            "select ?s where {" + 
                "?s rdfs:subClassOf tto:TerrainTwinObject ." +
            "}" ;
        }
    },

    // get all TTObjRelation subclasses from tto ontology 
    TTOBJRELATIONS {

        public String toString() {
            return Prefix.RDFS.toString() +
            Prefix.TTO.toString() + 
            "select ?s where {" + 
                "?s rdfs:subClassOf tto:TTObjRelation ." +
            "}" ;
        }
    },

    // get all TTObjRelation subclasses from tto ontology inclusiv relating predicates and their ranges
    EXTENDED_TTOBJRELATIONS {

        public String toString() {
            return Prefix.RDFS.toString() +
            Prefix.TTO.toString() + 
            "select * where {" + 
                "?relation rdfs:subClassOf tto:TTObjRelation ." +
                "?predicate rdfs:domain ?relation ." +
                "?predicate rdfs:range ?range ." +
            "}" ;
        }
    },

    // get all subject models (Fachmodelle) from the datacat ontology
    SUBJECT_MODELS {

        public String toString() {
            return Prefix.RDF.toString() +
            Prefix.XTD.toString() + 
            "select ?s where {" + 
                "?s rdf:type xtd:XtdBag ." +
                "?r xtd:RelatingCollection ?s ." +
                "?r xtd:RelatedThings ?b ." +
                "?b rdf:type xtd:XtdBag ." +
            "}" ;
        }
    },

    // get all groups Gruppen) from the datacat ontology
    GROUPS {

        public String toString() {
            return Prefix.RDF.toString() +
            Prefix.XTD.toString() + 
            "select ?s where {" + 
                "?s rdf:type xtd:XtdBag ." +
                "?r xtd:RelatedThings ?s ." +
                "?r rdf:type xtd:XtdRelCollects ." +
            "}" ;
        }
    },

    // get all classes (Klassen) from the datacat ontology
    CLASSES {

        public String toString() {
            return Prefix.RDF.toString() +
            Prefix.XTD.toString() +
            "select ?s where {" + 
                "?s rdf:type xtd:XtdSubject ." +
            "}" ;
        }
    };

}
