package com.Microservices.GeometryHandler.repositories;

import com.Microservices.GeometryHandler.schemas.Solid;

import org.springframework.stereotype.Repository;

@Repository
public interface SolidRepository extends PostgresTableRepository<Solid> {

}
