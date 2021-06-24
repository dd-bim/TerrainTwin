package com.Microservices.TopologyService.schemas;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;

import lombok.Getter;
import lombok.Setter;

@Entity(name = "terraintwinv2.dtm_specialpoints")
@Getter
@Setter
public class SpecialPoints extends PostgresTables {

    @Column(name="point_id", nullable = false)
    private Integer sp_id;

    @Column(name="tin_id", nullable=false)
    private UUID tin_id;

    protected SpecialPoints(){

    }
    
    public SpecialPoints(int point_id, UUID tin_id, String geometry){
        this.sp_id = point_id;
        this.tin_id = tin_id;
        this.geometry = geometry;
    }
}
