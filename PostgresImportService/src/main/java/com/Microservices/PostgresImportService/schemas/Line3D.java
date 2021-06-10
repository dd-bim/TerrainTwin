package com.Microservices.PostgresImportService.schemas;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.SQLInsert;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "line_3d", schema = "terraintwinv2")
@SQLInsert(sql = "INSERT INTO terraintwinv2.line_3d (geometry, line_id, id) VALUES (ST_Transform(ST_GeomFromEWKT(?),25832),?,?)" )
@Getter
@Setter
public class Line3D {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(name="line_id", nullable = false)
    private Integer l_id;

    @Column(nullable=false)
    private String geometry;

    protected Line3D(){

    }
    
    public Line3D(int line_id, String geometry){
        this.l_id = line_id;
        this.geometry = geometry;
    }
}