package com.Microservices.TopologyService.schemas;

import javax.persistence.Column;
import javax.persistence.Entity;

import lombok.Getter;
import lombok.Setter;

@Entity(name = "terraintwinv2.polygon_3d")
@Getter
@Setter
public class Polygon3D extends PostgresTables{

    @Column
    protected int origin_id;

    protected Polygon3D() {

    }

    public Polygon3D(Integer origin_id, String geometry) {
        this.origin_id = origin_id;
        this.geometry = geometry;
    }
}