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
@Table(name = "dtm_embarkment", schema = "terraintwinv2")
@SQLInsert(sql = "INSERT INTO terraintwinv2.dtm_embarkment (geometry, embarkment_id, id) VALUES (ST_Transform(ST_GeomFromEWKT(?),25832),?,?)" )
@Getter
@Setter
public class Embarkment {

    @Id
    @GeneratedValue(strategy= GenerationType.SEQUENCE)
    private Integer id;

    @Column(name="embarkment_id", nullable = false)
    private Integer e_id;

    @Column(nullable=false)
    private String geometry;

    protected Embarkment(){

    }
    
    public Embarkment(int embarkment_id, String geometry){
        this.e_id = embarkment_id;
        this.geometry = geometry;
    }
}