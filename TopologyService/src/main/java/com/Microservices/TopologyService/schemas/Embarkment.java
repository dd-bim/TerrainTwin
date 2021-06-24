package com.Microservices.TopologyService.schemas;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;

import lombok.Getter;
import lombok.Setter;

@Entity(name = "terraintwinv2.dtm_embarkment")
@Getter
@Setter
public class Embarkment extends PostgresTables {

    @Column
    private UUID tin_id;

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