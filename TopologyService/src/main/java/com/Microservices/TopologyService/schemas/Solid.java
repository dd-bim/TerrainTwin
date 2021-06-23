package com.Microservices.TopologyService.schemas;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.SQLInsert;

import lombok.Getter;
import lombok.Setter;

@Entity(name = "terraintwinv2.solid")
@Table(name = "solid", schema = "terraintwinv2")
@SQLInsert(sql = "INSERT INTO terraintwinv2.solid (geometry, solid_id, id) VALUES (ST_Transform(ST_GeomFromEWKT(?),25832),?,?)" )
@Getter
@Setter
public class Solid {

    @Id
    @GeneratedValue
    private UUID id;

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
