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

@Entity
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

    @Column(nullable=false)
    private String geometry;


    protected Polygon() {

    }

    public Polygon(Integer surfaceID, String geometry) {
        this.surfaceID = surfaceID;
        this.geometry = geometry;
    }
}