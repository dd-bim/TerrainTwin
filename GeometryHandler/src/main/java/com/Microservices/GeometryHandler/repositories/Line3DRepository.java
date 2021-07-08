package com.Microservices.GeometryHandler.repositories;

import com.Microservices.GeometryHandler.schemas.Line3D;

import org.springframework.stereotype.Repository;

@Repository
public interface Line3DRepository extends PostgresTableRepository<Line3D> {

}

