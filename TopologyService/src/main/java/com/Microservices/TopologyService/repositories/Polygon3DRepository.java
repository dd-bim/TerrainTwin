package com.Microservices.TopologyService.repositories;

import com.Microservices.TopologyService.schemas.Polygon3D;

import org.springframework.stereotype.Repository;

@Repository
public interface Polygon3DRepository extends PostgresTableRepository<Polygon3D> {

}
