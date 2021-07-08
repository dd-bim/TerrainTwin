package com.Microservices.GeometryHandler.repositories;

import com.Microservices.GeometryHandler.schemas.SpecialPoints;

import org.springframework.stereotype.Repository;

@Repository
public interface SpecialPointsRepository extends PostgresTableRepository<SpecialPoints> {

}
