package com.Microservices.PostgresImportService.schemas;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.SQLInsert;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;

import lombok.Getter;
import lombok.Setter;

// Table Schema for import insert in Postgres

@Entity
// @Table(name="surfaces_rohdaten", schema="terraintwin")
@Table(name="polygon", schema="terraintwinv2")
@SQLInsert(sql = "INSERT INTO terraintwinv2.polygon (geometry, polygon_id, id) VALUES (ST_GeomFromEWKT(?),?,?)" )
@Getter
@Setter
public class Polygon {

    @Id
    @GeneratedValue(strategy= GenerationType.SEQUENCE)
    private Integer id;

    @Column(name="polygon_id", nullable = false)
    private Integer surfaceID;

    // @Column(nullable = false)
    // private Integer srid;

    @Column(nullable=false)
    private String geometry;


    protected Polygon() {

    }

    // public Surfaces(Integer surfaceID, String geometry, Integer srid) {
    //     this.surfaceID = surfaceID;
    //     this.geometry = geometry;
    //     this.srid = srid;
    // }
    public Polygon(Integer surfaceID, String geometry) {
        this.surfaceID = surfaceID;
        this.geometry = geometry;
    }
}