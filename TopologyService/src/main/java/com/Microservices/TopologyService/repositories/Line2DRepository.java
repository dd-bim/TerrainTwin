package com.Microservices.TopologyService.repositories;

import com.Microservices.TopologyService.schemas.Line2D;

import org.springframework.stereotype.Repository;

@Repository
public interface Line2DRepository extends PostgresTableRepository<Line2D> {

}

