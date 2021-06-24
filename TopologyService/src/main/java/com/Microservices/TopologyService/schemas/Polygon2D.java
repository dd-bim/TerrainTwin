package com.Microservices.TopologyService.schemas;

import javax.persistence.Column;
import javax.persistence.Entity;

import lombok.Getter;
import lombok.Setter;

@Entity(name = "terraintwinv2.polygon_2d")
@Getter
@Setter
public class Polygon2D extends PostgresTables {

    @Column
    protected int origin_id;

    protected Polygon2D() {

    }

    public Polygon2D(int origin_id, String geometry) {
        this.origin_id = origin_id;
        this.geometry = geometry;
    }
}