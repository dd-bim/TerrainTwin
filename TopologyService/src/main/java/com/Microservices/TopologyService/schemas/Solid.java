package com.Microservices.TopologyService.schemas;

import javax.persistence.Column;
import javax.persistence.Entity;

import lombok.Getter;
import lombok.Setter;

@Entity(name = "terraintwinv2.solid")
@Getter
@Setter
public class Solid extends PostgresTables {

    @Column
    protected int origin_id;

    protected Solid(){

    }
    
    public Solid(int origin_id, String geometry){
        this.origin_id = origin_id;
        this.geometry = geometry;
    }
}
