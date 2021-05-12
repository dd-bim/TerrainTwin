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
@Table(name = "solid", schema = "terraintwinv2")
@SQLInsert(sql = "INSERT INTO terraintwinv2.solid (geometry, solid_id, id) VALUES (ST_GeomFromEWKT(?),?,?)" )
@Getter
@Setter
public class Solid {

    @Id
    @GeneratedValue(strategy= GenerationType.SEQUENCE)
    private Integer id;

    @Column(name="solid_id", nullable = false)
    private Integer s_id;

    @Column(nullable=false)
    private String geometry;

    protected Solid(){

    }
    
    public Solid(int solid_id, String geometry){
        this.s_id = solid_id;
        this.geometry = geometry;
    }
}
