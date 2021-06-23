package com.Microservices.TopologyService.schemas;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.SQLInsert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lombok.Getter;
import lombok.Setter;

@Entity(name = "terraintwinv2.dtm_breaklines")
@Table(name="dtm_breaklines", schema="terraintwinv2")
@SQLInsert(sql = "INSERT INTO terraintwinv2.dtm_breaklines (geometry, tin_id, bl_id) VALUES (ST_Transform(ST_GeomFromEWKT(?),25832),?,?)" )
@Getter
@Setter
public class Breaklines {

    private static Logger LOGGER = LoggerFactory.getLogger(Breaklines.class);
    
    @Id
    @GeneratedValue
    private UUID bl_id;

    @Column(nullable = false)
    private UUID tin_id;

    @Column(nullable=false)
    private String geometry;
  
    protected Breaklines() {

    }

    public Breaklines(UUID tin_id, String geometry){
        LOGGER.debug("Creating a breakline");
        this.tin_id = tin_id;
        this.geometry = geometry;
    }
}
