package com.Microservices.GeometryHandler.repositories;

import com.Microservices.GeometryHandler.schemas.Point2D;

import org.springframework.stereotype.Repository;

@Repository
public interface Point2DRepository extends PostgresTableRepository<Point2D> {

}
