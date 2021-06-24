package com.Microservices.PostgresImportService.schemas;

import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.SQLInsert;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name="dtm_tin", schema="terraintwinv2")
@SQLInsert(sql = "INSERT INTO terraintwinv2.dtm_tin (geometry, id) VALUES (ST_Transform(ST_GeomFromEWKT(?),25832),?)" )
@Getter
@Setter
public class TIN extends PostgresTables {

    protected TIN() {

    }

    public TIN(String geometry){
        this.geometry = geometry;
    }
    
}
