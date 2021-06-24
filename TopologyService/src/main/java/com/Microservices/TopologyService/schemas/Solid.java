package com.Microservices.TopologyService.schemas;

import javax.persistence.Column;
import javax.persistence.Entity;

import lombok.Getter;
import lombok.Setter;

@Entity(name = "terraintwinv2.solid")
@Getter
@Setter
public class Solid extends PostgresTables {

    @Column(name="solid_id", nullable = false)
    private Integer s_id;

    protected Solid(){

    }
    
    public Solid(int solid_id, String geometry){
        this.s_id = solid_id;
        this.geometry = geometry;
    }
}
