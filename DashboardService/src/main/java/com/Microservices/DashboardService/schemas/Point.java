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
@Table(name = "points", schema = "terraintwinv2")
@SQLInsert(sql = "INSERT INTO terraintwinv2.points (geometry, point_id, id) VALUES (ST_Transform(ST_GeomFromEWKT(?),25832),?,?)" )
@Getter
@Setter
public class Point {

    @Id
    @GeneratedValue(strategy= GenerationType.SEQUENCE)
    private Integer id;

    @Column(name="point_id", nullable = false)
    private Integer p_id;

    @Column(nullable=false)
    private String geometry;

    protected Point(){

    }
    
    public Point(int point_id, String geometry){
        this.p_id = point_id;
        this.geometry = geometry;
    }
}
