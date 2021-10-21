package com.Microservices.SemanticConnector.domain.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Instance {
    private String instance;
    private String className;

    public Instance(String instance, String className) {
        this.instance = instance;
        this.className = className;
    }

}
