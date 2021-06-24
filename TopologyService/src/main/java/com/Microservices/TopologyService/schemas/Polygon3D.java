package com.Microservices.TopologyService.schemas;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import lombok.Getter;
import lombok.Setter;

@Entity(name = "terraintwinv2.polygon_3d")
@Getter
@Setter
public class Polygon3D extends PostgresTables{

    // @Id
    // @GeneratedValue
    // private UUID id;

    @Column(name="polygon_id", nullable = false)
    private Integer surfaceID;

    // @Column(nullable=false)
    // private String geometry;


    protected Polygon3D() {

    }

    public Polygon3D(Integer surfaceID, String geometry) {
        this.surfaceID = surfaceID;
        this.geometry = geometry;
    }
}