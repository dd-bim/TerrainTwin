package com.Microservices.TopologyService.repositories;

import com.Microservices.TopologyService.schemas.Embarkment;

import org.springframework.stereotype.Repository;

@Repository
public interface EmbarkmentRepository extends PostgresTableRepository<Embarkment> {

}
