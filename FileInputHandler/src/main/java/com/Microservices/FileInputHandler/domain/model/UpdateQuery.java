package com.Microservices.FileInputHandler.domain.model;

import java.util.HashMap;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateQuery {

    private HashMap<String, String> namespaces;
    private List<TriplePair> pairs;

    public UpdateQuery() {

    }

    public UpdateQuery(HashMap<String, String> namespaces, List<TriplePair> pairs) {
        this.namespaces = namespaces;
        this.pairs = pairs;
    }
    
}
