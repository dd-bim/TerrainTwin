package com.Microservices.FileInputHandler.domain.model;

import java.util.HashMap;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DeleteQuery {

    private HashMap<String, String> namespaces;
    private List<Triple> triples;

    public DeleteQuery() {

    }

    public DeleteQuery(HashMap<String, String> namespaces, List<Triple> triples) {
        this.namespaces = namespaces;
        this.triples = triples;
    }
    
}
