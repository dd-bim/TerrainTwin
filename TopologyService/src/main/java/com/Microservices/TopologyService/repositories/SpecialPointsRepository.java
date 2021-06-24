package com.Microservices.TopologyService.repositories;

import com.Microservices.TopologyService.schemas.SpecialPoints;

import org.springframework.stereotype.Repository;

@Repository
public interface SpecialPointsRepository extends PostgresTableRepository<SpecialPoints> {

}
