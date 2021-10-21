package com.Microservices.SemanticConnector.domain.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Relation {
    private String relation;
    private String instance1;
    private String predicate1;
    private String instance2;
    private String predicate2;


    public Relation(String relation, String instance1, String predicate1, String instance2, String predicate2) {
        this.relation = relation;
        this.instance1 = instance1;
        this.predicate1 = predicate1;
        this.instance2 = instance2;
        this.predicate2 = predicate2;
    }

}
