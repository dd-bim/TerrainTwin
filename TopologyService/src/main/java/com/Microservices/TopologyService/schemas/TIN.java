package com.Microservices.TopologyService.schemas;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import lombok.Getter;
import lombok.Setter;

@Entity(name = "terraintwinv2.dtm_tin")
@Getter
@Setter
public class TIN extends PostgresTables{
    
    // @Id
    // @GeneratedValue
    // private UUID id;

    // @Column(nullable=false)
    // private String geometry;

    protected TIN() {

    }

    public TIN(String geometry){
        this.geometry = geometry;
    }
    
}
