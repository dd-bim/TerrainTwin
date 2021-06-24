package com.Microservices.TopologyService.schemas;

import javax.persistence.Column;
import javax.persistence.Entity;

import lombok.Getter;
import lombok.Setter;

@Entity(name = "terraintwinv2.point_2d")
@Getter
@Setter
public class Point2D extends PostgresTables {

    @Column
    protected int origin_id;

    protected Point2D(){

    }
    
    public Point2D(int origin_id, String geometry){
        this.origin_id = origin_id;
        this.geometry = geometry;
    }
}
