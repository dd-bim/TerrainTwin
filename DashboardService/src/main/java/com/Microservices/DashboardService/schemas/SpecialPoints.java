package com.Microservices.DashboardService.schemas;

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
@Table(name = "dtm_specialpoints", schema = "terraintwinv2")
@SQLInsert(sql = "INSERT INTO terraintwinv2.dtm_specialpoints (geometry, point_id, tin_id, id) VALUES (ST_Transform(ST_GeomFromEWKT(?),25832),?,?,?)" )
@Getter
@Setter
public class SpecialPoints {

    @Id
    @GeneratedValue(strategy= GenerationType.SEQUENCE)
    private Integer id;

    @Column(name="point_id", nullable = false)
    private Integer sp_id;

    @Column(name="tin_id", nullable=false)
    private Integer tin_id;

    @Column(nullable=false)
    private String geometry;

    protected SpecialPoints(){

    }
    
    public SpecialPoints(int point_id, Integer tin_id, String geometry){
        this.sp_id = point_id;
        this.tin_id = tin_id;
        this.geometry = geometry;
    }
}
