package com.Microservices.TopologyService.schemas;

import javax.persistence.Column;
import javax.persistence.Entity;

import lombok.Getter;
import lombok.Setter;

@Entity(name = "terraintwinv2.polygon_2d")
@Getter
@Setter
public class Polygon2D extends PostgresTables {

    @Column(name="polygon_id", nullable = false)
    private Integer surfaceID;

    protected Polygon2D() {

    }

    public Polygon2D(Integer surfaceID, String geometry) {
        this.surfaceID = surfaceID;
        this.geometry = geometry;
    }
}