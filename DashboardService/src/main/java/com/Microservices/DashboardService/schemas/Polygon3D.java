package com.Microservices.DashboardService.schemas;

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
@Table(name="polygon_3d", schema="terraintwinv2")
@SQLInsert(sql = "INSERT INTO terraintwinv2.polygon_3d (geometry, polygon_id, id) VALUES (ST_Transform(ST_GeomFromEWKT(?),25832),?,?)" )
@Getter
@Setter
public class Polygon3D {

    @Id
    @GeneratedValue(strategy= GenerationType.SEQUENCE)
    private Integer id;

    @Column(name="polygon_id", nullable = false)
    private Integer surfaceID;

    @Column(nullable=false)
    private String geometry;


    protected Polygon3D() {

    }

    public Polygon3D(Integer surfaceID, String geometry) {
        this.surfaceID = surfaceID;
        this.geometry = geometry;
    }
}