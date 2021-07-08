package com.Microservices.GeometryHandler.schemas;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.SQLInsert;

import lombok.Getter;
import lombok.Setter;

@Entity(name = "terraintwinv2.dtm_embarkment")
@Table(name = "dtm_embarkment", schema = "terraintwinv2")
@SQLInsert(sql = "INSERT INTO terraintwinv2.dtm_embarkment (geometry, tin_id, id) VALUES (ST_Transform(ST_GeomFromEWKT(?),25832),?,?)" )
@Getter
@Setter
public class Embarkment extends PostgresTables {

    @Column
    private UUID tin_id;

    protected Embarkment(){

    }

    public Embarkment(String geometry){
        this.geometry = geometry;
    }

    public Embarkment(UUID tin_id, String geometry){
        this.tin_id = tin_id;
        this.geometry = geometry;
    }
}