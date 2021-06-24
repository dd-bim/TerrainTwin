package com.Microservices.TopologyService.repositories;

import com.Microservices.TopologyService.schemas.Point3D;

import org.springframework.stereotype.Repository;

@Repository
public interface Point3DRepository extends PostgresTableRepository<Point3D> {
    
}
