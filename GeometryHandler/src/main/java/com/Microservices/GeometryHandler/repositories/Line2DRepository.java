package com.Microservices.GeometryHandler.repositories;

import com.Microservices.GeometryHandler.schemas.Line2D;

import org.springframework.stereotype.Repository;

@Repository
public interface Line2DRepository extends PostgresTableRepository<Line2D> {

}

