package com.Microservices.TopologyService.schemas;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lombok.Getter;
import lombok.Setter;

@Entity(name = "terraintwinv2.dtm_breaklines")
@Getter
@Setter
public class Breaklines extends PostgresTables {
    
    // @Id
    // @GeneratedValue
    // private UUID id;

    @Column(nullable = false)
    private UUID tin_id;

    // @Column(nullable=false)
    // private String geometry;
  
    protected Breaklines() {

    }

    public Breaklines(UUID tin_id, String geometry){
        this.tin_id = tin_id;
        this.geometry = geometry;
    }
}
