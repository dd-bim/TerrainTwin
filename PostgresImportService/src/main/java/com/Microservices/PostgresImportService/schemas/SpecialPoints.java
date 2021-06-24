package com.Microservices.PostgresImportService.schemas;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.SQLInsert;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "dtm_specialpoints", schema = "terraintwinv2")
@SQLInsert(sql = "INSERT INTO terraintwinv2.dtm_specialpoints (geometry, origin_id, tin_id, id) VALUES (ST_Transform(ST_GeomFromEWKT(?),25832),?,?,?)" )
@Getter
@Setter
public class SpecialPoints extends PostgresTables {

    @Column
    protected int origin_id;

    @Column(name="tin_id", nullable=false)
    private UUID tin_id;

    protected SpecialPoints(){

    }
    
    public SpecialPoints(int origin_id, UUID tin_id, String geometry){
        this.origin_id = origin_id;
        this.tin_id = tin_id;
        this.geometry = geometry;
    }
}
