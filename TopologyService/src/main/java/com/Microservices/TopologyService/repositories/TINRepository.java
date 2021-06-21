package com.Microservices.TopologyService.repositories;

import java.util.UUID;

import com.Microservices.TopologyService.schemas.TIN;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TINRepository extends CrudRepository<TIN, UUID> {

    @Query(value = "select ST_asEWKT(geometry) from terraintwinv2.dtm_tin where tin_id = '348bee04-67fa-4a46-a63d-1fc8c9f311c1'", nativeQuery = true)
    String getTINGeometry();
}
