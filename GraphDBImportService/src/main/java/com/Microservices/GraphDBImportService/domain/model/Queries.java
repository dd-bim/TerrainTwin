package com.Microservices.GraphDBImportService.domain.model;

import java.util.HashMap;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Queries {

    private HashMap<String, String> namespaces;
    private List<Triple> triples;

    public Queries() {

    }

    public Queries(HashMap<String, String> namespaces, List<Triple> triples) {
        this.namespaces = namespaces;
        this.triples = triples;
    }
    
}
