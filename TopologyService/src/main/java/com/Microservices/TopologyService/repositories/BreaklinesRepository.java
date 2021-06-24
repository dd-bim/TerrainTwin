package com.Microservices.TopologyService.repositories;

import com.Microservices.TopologyService.schemas.Breaklines;

import org.springframework.stereotype.Repository;

@Repository
public interface BreaklinesRepository extends PostgresTableRepository<Breaklines> {

}
