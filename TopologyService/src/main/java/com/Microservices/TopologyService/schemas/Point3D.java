package com.Microservices.TopologyService.schemas;

import javax.persistence.Column;
import javax.persistence.Entity;

import lombok.Getter;
import lombok.Setter;

@Entity(name = "terraintwinv2.point_3d")
@Getter
@Setter
public class Point3D extends PostgresTables {

    @Column(name = "point_id", nullable = false)
    private Integer p_id;

    protected Point3D() {

    }

    public Point3D(int point_id, String geometry) {
        this.p_id = point_id;
        this.geometry = geometry;
    }
}
