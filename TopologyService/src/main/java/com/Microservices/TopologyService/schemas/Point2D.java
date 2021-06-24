package com.Microservices.TopologyService.schemas;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import lombok.Getter;
import lombok.Setter;

@Entity(name = "terraintwinv2.point_2d")
@Getter
@Setter
public class Point2D extends PostgresTables {

    // @Id
    // @GeneratedValue
    // private UUID id;

    @Column(name="point_id", nullable = false)
    private Integer p_id;

    // @Column(nullable=false)
    // private String geometry;

    protected Point2D(){

    }
    
    public Point2D(int point_id, String geometry){
        this.p_id = point_id;
        this.geometry = geometry;
    }
}
