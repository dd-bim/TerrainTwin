package com.Microservices.PostgresImportService.schemas;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;

import lombok.Getter;
import lombok.Setter;

// Table Schema for import insert in Postgres

@Entity
@Table(name="surfaces_rohdaten", schema="terraintwin")
@Getter
@Setter
public class Surfaces {

    @Id
    @GeneratedValue(strategy= GenerationType.SEQUENCE)
    private Integer id;

    @Column(name="surface_id", nullable = false)
    private Integer surfaceID;

    @Column(name="wkt", nullable=false)
    private String geometry;

    @Column(nullable = false)
    private Integer srid;

    protected Surfaces() {

    }

    public Surfaces(Integer surfaceID, String geometry, Integer srid) {
        this.surfaceID = surfaceID;
        this.geometry = geometry;
        this.srid = srid;
    }
}