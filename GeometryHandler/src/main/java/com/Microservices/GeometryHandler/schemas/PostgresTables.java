package com.Microservices.GeometryHandler.schemas;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

import lombok.Getter;
import lombok.Setter;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@Getter
@Setter
public abstract class PostgresTables {

    @Id
    @GeneratedValue
    protected UUID id;

    // @Column
    // protected int origin_id;

    @Column(nullable=false)
    protected String geometry;

}
