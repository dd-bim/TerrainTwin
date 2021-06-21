package com.Microservices.TopologyService.schemas;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.SQLInsert;
import org.locationtech.jts.geom.Polygon;

import javax.persistence.GeneratedValue;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name="polygon_2d", schema="terraintwinv2")
// @SQLInsert(sql = "INSERT INTO terraintwinv2.polygon_2d (geometry, polygon_id, id) VALUES (ST_Transform(ST_GeomFromEWKT(?),25832),?,?)" )
@Getter
@Setter
public class Polygon2D {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(name="polygon_id", nullable = false)
    private Integer surfaceID;

    @Column(columnDefinition = "org.postgis.Polygon" ,nullable=false)
    private Polygon geometry;


    protected Polygon2D() {

    }

    public Polygon2D(Integer surfaceID, Polygon geometry) {
        this.surfaceID = surfaceID;
        this.geometry = geometry;
    }
}