package com.Microservices.TopologyService.schemas;

import javax.persistence.Entity;

import lombok.Getter;
import lombok.Setter;

@Entity(name = "terraintwinv2.dtm_tin")
@Getter
@Setter
public class TIN extends PostgresTables{

    protected TIN() {

    }

    public TIN(String geometry){
        this.geometry = geometry;
    }
    
}
