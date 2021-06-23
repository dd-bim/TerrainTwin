package com.Microservices.TopologyService.schemas;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.SQLInsert;
import org.locationtech.jts.geom.Point;

import lombok.Getter;
import lombok.Setter;

@Entity(name = "terraintwinv2.point_2d")
@Table(name = "point_2d", schema = "terraintwinv2")
// @SQLInsert(sql = "INSERT INTO terraintwinv2.point_2d (geometry, point_id, id) VALUES (ST_Transform(ST_GeomFromEWKT(?),25832),?,?)" )
@Getter
@Setter
public class Point2D {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(name="point_id", nullable = false)
    private Integer p_id;

    @Column(nullable=false)
    private String geometry;

    // @Column(columnDefinition = "org.postgis.Point" ,nullable=false)
    // private Point geometry;

    protected Point2D(){

    }
    
    public Point2D(int point_id, String geometry){
        this.p_id = point_id;
        this.geometry = geometry;
    }
}
