package com.Microservices.TopologyService.schemas;

import javax.persistence.Column;
import javax.persistence.Entity;

import lombok.Getter;
import lombok.Setter;

@Entity(name = "terraintwinv2.point_3d")
@Getter
@Setter
public class Point3D extends PostgresTables {

    @Column
    protected int origin_id;

    protected Point3D() {

    }

    public Point3D(int origin_id, String geometry) {
        this.origin_id = origin_id;
        this.geometry = geometry;
    }
}
