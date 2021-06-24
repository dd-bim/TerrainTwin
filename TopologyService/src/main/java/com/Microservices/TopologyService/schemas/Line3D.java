package com.Microservices.TopologyService.schemas;

import javax.persistence.Column;
import javax.persistence.Entity;

import lombok.Getter;
import lombok.Setter;

@Entity(name = "terraintwinv2.line_3d")
@Getter
@Setter
public class Line3D extends PostgresTables {

    @Column(name="line_id", nullable = false)
    private Integer l_id;

    protected Line3D(){

    }
    
    public Line3D(int line_id, String geometry){
        this.l_id = line_id;
        this.geometry = geometry;
    }
}