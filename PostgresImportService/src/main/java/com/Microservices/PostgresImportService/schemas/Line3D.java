package com.Microservices.PostgresImportService.schemas;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.SQLInsert;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "line_3d", schema = "terraintwinv2")
@SQLInsert(sql = "INSERT INTO terraintwinv2.line_3d (geometry, origin_id, id) VALUES (ST_Transform(ST_GeomFromEWKT(?),25832),?,?)" )
@Getter
@Setter
public class Line3D extends PostgresTables {

    @Column
    protected int origin_id;

    protected Line3D(){

    }
    
    public Line3D(int origin_id, String geometry){
        this.origin_id = origin_id;
        this.geometry = geometry;
    }
}