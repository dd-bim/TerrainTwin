package com.Microservices.GeometryHandler.repositories;

import com.Microservices.GeometryHandler.schemas.Point3D;

import org.springframework.stereotype.Repository;

@Repository
public interface Point3DRepository extends PostgresTableRepository<Point3D> {

}
