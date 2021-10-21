package com.Microservices.SemanticConnector.domain;

import org.springframework.stereotype.Service;

@Service
public class FlexibleQuery {
    
    // find all groups in a subject model
    public String findGroupsInSubjectModel(String subjectModel) {
        return Prefix.RDF.toString() +
            Prefix.XTD.toString() + 
            "select ?s where {" + 
                "?s rdf:type xtd:XtdBag ." +
                "?r xtd:RelatedThings ?s ." +
                "?r xtd:RelatingCollection <"+ subjectModel +"> ." +
            "}" ;   
    }

    //find all classes in a group
    public String findClassesInGroup(String group) {
        return Prefix.RDF.toString() +
            Prefix.XTD.toString() + 
            "select ?s where {" + 
                "?s rdf:type xtd:XtdSubject ." +
                "?r xtd:RelatedThings ?s ." +
                "?r xtd:RelatingCollection <" + group + "> ." +
            "}" ;
        }

    //find all predicates with domain relation
    public String findPredicatesFromRelation(String relation) {
        return Prefix.RDFS.toString() +
            Prefix.TTO.toString() + 
            "select ?s where {" + 
                "?s rdfs:domain <" + relation + "> ." +
            "}" ;
        }
}
