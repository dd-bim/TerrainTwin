package com.Microservices.TopologyService.repositories;

import com.Microservices.TopologyService.schemas.Line3D;

import org.springframework.stereotype.Repository;

@Repository
public interface Line3DRepository extends PostgresTableRepository<Line3D> {

}

