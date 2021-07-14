package com.Microservices.FileInputHandler.domain.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TriplePair {
    
    private Triple oldTriple;
    private Triple newTriple;

    public TriplePair(Triple oldTriple, Triple newTriple) {

        this.oldTriple = oldTriple;
        this.newTriple = newTriple;
    }
}
