package com.Microservices.DashboardService.schemas;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.SQLInsert;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "solid", schema = "terraintwinv2")
@SQLInsert(sql = "INSERT INTO terraintwinv2.solid (geometry, origin_id, id) VALUES (ST_Transform(ST_GeomFromEWKT(?),25832),?,?)" )
@Getter
@Setter
public class Solid extends PostgresTables {

    @Column
    protected int origin_id;

    protected Solid(){

    }
    
    public Solid(int origin_id, String geometry){
        this.origin_id = origin_id;
        this.geometry = geometry;
    }
}
