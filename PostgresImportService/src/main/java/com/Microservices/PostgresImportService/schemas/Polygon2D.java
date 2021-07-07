package com.Microservices.PostgresImportService.schemas;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.SQLInsert;

import lombok.Getter;
import lombok.Setter;

@Entity(name = "terraintwinv2.polygon_2d")
@Table(name="polygon_2d", schema="terraintwinv2")
@SQLInsert(sql = "INSERT INTO terraintwinv2.polygon_2d (geometry, origin_id, id) VALUES (ST_Transform(ST_GeomFromEWKT(?),25832),?,?)" )
@Getter
@Setter
public class Polygon2D extends PostgresTables {

    @Column
    protected int origin_id;

    protected Polygon2D() {

    }

    public Polygon2D(Integer origin_id, String geometry) {
        this.origin_id = origin_id;
        this.geometry = geometry;
    }
}