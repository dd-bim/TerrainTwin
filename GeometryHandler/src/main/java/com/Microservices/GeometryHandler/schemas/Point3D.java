package com.Microservices.GeometryHandler.schemas;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.SQLInsert;

import lombok.Getter;
import lombok.Setter;

@Entity(name = "terraintwinv2.point_3d")
@Table(name = "point_3d", schema = "terraintwinv2")
@SQLInsert(sql = "INSERT INTO terraintwinv2.point_3d (geometry, origin_id, id) VALUES (ST_Transform(ST_GeomFromEWKT(?),25832),?,?)")
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

    public Point3D(UUID id, int origin_id, String geometry) {
        this.id = id;
        this.origin_id = origin_id;
        this.geometry = geometry;
    }
}
