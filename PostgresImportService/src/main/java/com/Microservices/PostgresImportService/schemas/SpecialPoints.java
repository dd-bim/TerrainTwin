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
@Table(name = "dtm_specialPoints", schema = "terraintwinv2")
@SQLInsert(sql = "INSERT INTO terraintwinv2.dtm_specialPoints (geometry, point_id, id) VALUES (ST_GeomFromEWKT(?),?,?)" )
@Getter
@Setter
public class SpecialPoints {

    @Id
    @GeneratedValue(strategy= GenerationType.SEQUENCE)
    private Integer id;

    @Column(name="point_id", nullable = false)
    private Integer sp_id;

    @Column(nullable=false)
    private String geometry;

    protected SpecialPoints(){

    }
    
    public SpecialPoints(int point_id, String geometry){
        this.sp_id = point_id;
        this.geometry = geometry;
    }
}
