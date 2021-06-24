package com.Microservices.TopologyService.repositories;

import com.Microservices.TopologyService.schemas.Solid;

import org.springframework.stereotype.Repository;

@Repository
public interface SolidRepository extends PostgresTableRepository<Solid> {

}
