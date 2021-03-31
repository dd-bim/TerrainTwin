package com.Microservices.PostgresImportService.schemas;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name="tin_rohdaten", schema="terraintwin")
@Getter
@Setter
public class TIN {
    
    @Id
    @GeneratedValue(strategy= GenerationType.SEQUENCE)
    private Integer tin_id;

    @Column(name="wkt", nullable=false)
    private String geometry;

    @Column(nullable = false)
    private Integer srid;

    protected TIN() {

    }

    public TIN(String geometry, Integer srid){
        this.geometry = geometry;
        this.srid = srid;
    }
    
}
