package com.Microservices.TopologyService.repositories;

import com.Microservices.TopologyService.schemas.Point2D;

import org.springframework.stereotype.Repository;

@Repository
public interface Point2DRepository extends PostgresTableRepository<Point2D> {

}
