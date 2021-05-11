package com.Microservices.PostgresImportService.schemas;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.SQLInsert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name="dtm_breaklines", schema="terraintwinv2")
@SQLInsert(sql = "INSERT INTO terraintwinv2.dtm_breaklines (geometry, tin_id, bl_id) VALUES (ST_GeomFromEWKT(?),?,?)" )
@Getter
@Setter
public class Breaklines {

    private static Logger LOGGER = LoggerFactory.getLogger(Breaklines.class);
    
    @Id
    @GeneratedValue(strategy= GenerationType.SEQUENCE)
    private Integer bl_id;

    // @Column(nullable = false)
    // private Integer srid;

    @Column(nullable = false)
    private Integer tin_id;

    @Column(name="geometry", nullable=false)
    private String geometry;
  
    protected Breaklines() {

    }

    // public Breaklines(String geometry, Integer srid, Integer tin_id){
    //     LOGGER.debug("Creating a breakline");
    //     this.geometry = geometry;
    //     this.srid = srid;
    //     this.tin_id = tin_id;
    // }
    public Breaklines(Integer tin_id, String geometry){
        LOGGER.debug("Creating a breakline");
        this.tin_id = tin_id;
        this.geometry = geometry;
    }
}
