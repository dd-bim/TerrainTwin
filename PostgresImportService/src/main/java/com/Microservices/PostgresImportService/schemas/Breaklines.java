package com.Microservices.PostgresImportService.schemas;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.SQLInsert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lombok.Getter;
import lombok.Setter;

@Entity(name = "terraintwinv2.dtm_breaklines")
@Table(name="dtm_breaklines", schema="terraintwinv2")
@SQLInsert(sql = "INSERT INTO terraintwinv2.dtm_breaklines (geometry, tin_id, id) VALUES (ST_Transform(ST_GeomFromEWKT(?),25832),?,?)" )
@Getter
@Setter
public class Breaklines extends PostgresTables {

    private static Logger LOGGER = LoggerFactory.getLogger(Breaklines.class);

    @Column(nullable = false)
    private UUID tin_id;
  
    protected Breaklines() {

    }

    public Breaklines(UUID tin_id, String geometry){
        LOGGER.debug("Creating a breakline");
        this.tin_id = tin_id;
        this.geometry = geometry;
    }
}
