package com.Microservices.TopologyService.schemas;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;

import lombok.Getter;
import lombok.Setter;

@Entity(name = "terraintwinv2.dtm_breaklines")
@Getter
@Setter
public class Breaklines extends PostgresTables {

    @Column(nullable = false)
    private UUID tin_id;
  
    protected Breaklines() {

    }

    public Breaklines(UUID tin_id, String geometry){
        this.tin_id = tin_id;
        this.geometry = geometry;
    }
}
