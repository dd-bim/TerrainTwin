package com.Microservices.GeometryHandler.schemas;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.SQLInsert;

import lombok.Getter;
import lombok.Setter;

@Entity(name = "terraintwinv2.polygon_3d")
@Table(name="polygon_3d", schema="terraintwinv2")
@SQLInsert(sql = "INSERT INTO terraintwinv2.polygon_3d (geometry, origin_id, id) VALUES (ST_Transform(ST_GeomFromEWKT(?),25832),?,?)" )
@Getter
@Setter
public class Polygon3D extends PostgresTables {

    @Column
    protected int origin_id;

    protected Polygon3D() {

    }

    public Polygon3D(Integer origin_id, String geometry) {
        this.origin_id = origin_id;
        this.geometry = geometry;
    }
}