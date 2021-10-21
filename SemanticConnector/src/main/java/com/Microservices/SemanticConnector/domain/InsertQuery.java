package com.Microservices.SemanticConnector.domain;

import java.util.UUID;

import com.Microservices.SemanticConnector.domain.model.Instance;
import com.Microservices.SemanticConnector.domain.model.Relation;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class InsertQuery {
    
    @Value("${domain.url}") 
    String url;

    // add a class definition to a GraphDB repository
    public String insertClassDefinition(Instance instance) {
        return Prefix.RDF.toString() +
            "insert {" + 
                "<" + instance.getInstance() + "> rdf:type <" + instance.getClassName() + "> ." +
            "} where {}" ;   
    }

    // add a TTObjRelation to the GraphDB repository
    public String insertTTObjRelation(Relation relation) {
        String relInstance = url + "/project/" + UUID.randomUUID().toString();
        
        return Prefix.RDF.toString() +
            "insert {" + 
                "<" + relInstance + "> rdf:type <" + relation.getRelation() + "> ." +
                "<" + relInstance + "> <" + relation.getPredicate1() + "> <" + relation.getInstance1() + "> ." + 
                "<" + relInstance + "> <" + relation.getPredicate2() + "> <" + relation.getInstance2() + "> ." + 
            "} where {}" ;   
    }
}
