package com.Microservices.TopologyService.schemas;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.locationtech.jts.geom.Polygon;

import lombok.Getter;
import lombok.Setter;

@Entity(name = "terraintwinv2.polygon_2d")
@Getter
@Setter
public class Polygon2D extends PostgresTables {

    // @Id
    // @GeneratedValue
    // private UUID id;

    @Column(name="polygon_id", nullable = false)
    private Integer surfaceID;

    // @Column(nullable=false)
    // private String geometry;


    protected Polygon2D() {

    }

    public Polygon2D(Integer surfaceID, String geometry) {
        this.surfaceID = surfaceID;
        this.geometry = geometry;
    }
}