package com.Microservices.DashboardService.schemas;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.SQLInsert;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "point_2d", schema = "terraintwinv2")
@SQLInsert(sql = "INSERT INTO terraintwinv2.point_2d (geometry, origin_id, id) VALUES (ST_Transform(ST_GeomFromEWKT(?),25832),?,?)" )
@Getter
@Setter
public class Point2D extends PostgresTables {

    @Column
    protected int origin_id;

    protected Point2D(){

    }
    
    public Point2D(int origin_id, String geometry){
        this.origin_id = origin_id;
        this.geometry = geometry;
    }
}
