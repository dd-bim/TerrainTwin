package com.Microservices.TopologyService.schemas;

import javax.persistence.Column;
import javax.persistence.Entity;

import lombok.Getter;
import lombok.Setter;

@Entity(name = "terraintwinv2.line_3d")
@Getter
@Setter
public class Line3D extends PostgresTables {

    @Column
    protected int origin_id;

    protected Line3D(){

    }
    
    public Line3D(int origin_id, String geometry){
        this.origin_id = origin_id;
        this.geometry = geometry;
    }
}