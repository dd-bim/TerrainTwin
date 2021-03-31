package com.Microservices.PostgresImportService.schemas;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name="breaklines_rohdaten", schema="terraintwin")
@Getter
@Setter
public class Breaklines {

    private static Logger LOGGER = LoggerFactory.getLogger(Breaklines.class);
    
    @Id
    @GeneratedValue(strategy= GenerationType.SEQUENCE)
    private Integer bl_id;

    @Column(name="wkt", nullable=false)
    private String geometry;

    @Column(nullable = false)
    private Integer srid;

    @Column(nullable = false)
    private Integer tin_id;
  
    protected Breaklines() {

    }

    public Breaklines(String geometry, Integer srid, Integer tin_id){
        LOGGER.debug("Creating a breakline");
        this.geometry = geometry;
        this.srid = srid;
        this.tin_id = tin_id;
    }
}
