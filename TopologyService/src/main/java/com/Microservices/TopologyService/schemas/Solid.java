package com.Microservices.TopologyService.schemas;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import lombok.Getter;
import lombok.Setter;

@Entity(name = "terraintwinv2.solid")
@Getter
@Setter
public class Solid extends PostgresTables {

    // @Id
    // @GeneratedValue
    // private UUID id;

    @Column(name="solid_id", nullable = false)
    private Integer s_id;

    // @Column(nullable=false)
    // private String geometry;

    protected Solid(){

    }
    
    public Solid(int solid_id, String geometry){
        this.s_id = solid_id;
        this.geometry = geometry;
    }
}
