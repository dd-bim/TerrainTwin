package com.Microservices.GeometryHandler.repositories;

import com.Microservices.GeometryHandler.schemas.Polygon3D;

import org.springframework.stereotype.Repository;

@Repository
public interface Polygon3DRepository extends PostgresTableRepository<Polygon3D> {

}
