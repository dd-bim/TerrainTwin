package com.Microservices.TopologyService.repositories;

import com.Microservices.TopologyService.schemas.Polygon2D;

import org.springframework.stereotype.Repository;

@Repository
public interface Polygon2DRepository extends PostgresTableRepository<Polygon2D> {

}
