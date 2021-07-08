package com.Microservices.GeometryHandler.repositories;

import com.Microservices.GeometryHandler.schemas.Embarkment;

import org.springframework.stereotype.Repository;

@Repository
public interface EmbarkmentRepository extends PostgresTableRepository<Embarkment> {

}
