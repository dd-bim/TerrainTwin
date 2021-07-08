package com.Microservices.GeometryHandler.repositories;

import com.Microservices.GeometryHandler.schemas.Polygon2D;

import org.springframework.stereotype.Repository;

@Repository
public interface Polygon2DRepository extends PostgresTableRepository<Polygon2D> {

}
