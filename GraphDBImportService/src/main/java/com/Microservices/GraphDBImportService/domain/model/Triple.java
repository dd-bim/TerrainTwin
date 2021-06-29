package com.Microservices.GraphDBImportService.domain.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Triple {
    
    private String subject;
    private String predicate;
    private String object;

    public Triple(String subject, String predicate, String object) {
        this.subject = subject;
        this.predicate = predicate;
        this.object = object;
    }

}
