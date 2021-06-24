package com.Microservices.TopologyService.schemas;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import lombok.Getter;
import lombok.Setter;

@Entity(name = "terraintwinv2.dtm_embarkment")
@Getter
@Setter
public class Embarkment extends PostgresTables {

    // @Id
    // @GeneratedValue
    // private UUID id;

    @Column
    private UUID tin_id;

    // @Column(nullable=false)
    // private String geometry;

    protected Embarkment(){

    }

    public Embarkment(String geometry){
        this.geometry = geometry;
    }

    public Embarkment(UUID tin_id, String geometry){
        this.tin_id = tin_id;
        this.geometry = geometry;
    }
}