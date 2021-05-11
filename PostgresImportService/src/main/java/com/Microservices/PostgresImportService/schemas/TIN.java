package com.Microservices.PostgresImportService.schemas;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.SQLInsert;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name="dtm_tin", schema="terraintwinv2")
@SQLInsert(sql = "INSERT INTO terraintwinv2.dtm_tin (geometry, tin_id) VALUES (ST_GeomFromEWKT(?),?)" )
@Getter
@Setter
public class TIN {
    
    @Id
    @GeneratedValue(strategy= GenerationType.SEQUENCE)
    private Integer tin_id;

    @Column(name="geometry", nullable=false)
    private String geometry;

    // @Column(nullable = false)
    // private Integer srid;

    protected TIN() {

    }

    // public TIN(String geometry, Integer srid){
    //     this.geometry = geometry;
    //     this.srid = srid;
    // }
    public TIN(String geometry){
        this.geometry = geometry;
    }
    
}
