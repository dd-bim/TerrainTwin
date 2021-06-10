package com.Microservices.PostgresImportService.schemas;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.SQLInsert;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name="dtm_tin", schema="terraintwinv2")
@SQLInsert(sql = "INSERT INTO terraintwinv2.dtm_tin (geometry, tin_id) VALUES (ST_Transform(ST_GeomFromEWKT(?),25832),?)" )
@Getter
@Setter
public class TIN {
    
    @Id
    @GeneratedValue
    private UUID tin_id;

    @Column(nullable=false)
    private String geometry;

    protected TIN() {

    }

    public TIN(String geometry){
        this.geometry = geometry;
    }
    
}
