package com.Microservices.TopologyService.schemas;

import javax.persistence.Column;
import javax.persistence.Entity;

import lombok.Getter;
import lombok.Setter;

@Entity(name = "terraintwinv2.polygon_3d")
@Getter
@Setter
public class Polygon3D extends PostgresTables{

    @Column(name="polygon_id", nullable = false)
    private Integer surfaceID;

    protected Polygon3D() {

    }

    public Polygon3D(Integer surfaceID, String geometry) {
        this.surfaceID = surfaceID;
        this.geometry = geometry;
    }
}