package com.Microservices.TopologyService.repositories;

import com.Microservices.TopologyService.schemas.TIN;

import org.springframework.stereotype.Repository;

@Repository
public interface TINRepository extends PostgresTableRepository<TIN> {

}
