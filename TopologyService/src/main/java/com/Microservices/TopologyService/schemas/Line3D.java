package com.Microservices.TopologyService.schemas;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import lombok.Getter;
import lombok.Setter;

@Entity(name = "terraintwinv2.line_3d")
@Getter
@Setter
public class Line3D extends PostgresTables {

    // @Id
    // @GeneratedValue
    // private UUID id;

    @Column(name="line_id", nullable = false)
    private Integer l_id;

    // @Column(nullable=false)
    // private String geometry;

    protected Line3D(){

    }
    
    public Line3D(int line_id, String geometry){
        this.l_id = line_id;
        this.geometry = geometry;
    }
}