package com.Microservices.SemanticConnector.domain;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class InsertQuery {
    
    @Value("${domain.url}") 
    String url;

    // add a class definition to a GraphDB repository
    public String insertClassDefinition(String instance, String className) {
        return Prefix.RDF.toString() +
            "insert {" + 
                "<" + instance + "> rdf:type <" + className + "> ." +
            "} where {}" ;   
    }

    // add a TTObjRelation to the GraphDB repository
    public String insertTTObjRelation(String relation, String instance1, String instance2, String predicate1, String predicate2) {
        String relInstance = url + "/project/" + UUID.randomUUID().toString();
        
        return Prefix.RDF.toString() +
            "insert {" + 
                "<" + relInstance + "> rdf:type <" + relation + "> ." +
                "<" + relInstance + "> <" + predicate1 + "> <" + instance1 + "> ." + 
                "<" + relInstance + "> <" + predicate2 + "> <" + instance2 + "> ." + 
            "} where {}" ;   
    }
}
