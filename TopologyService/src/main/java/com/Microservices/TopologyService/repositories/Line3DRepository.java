package com.Microservices.TopologyService.repositories;

import java.util.UUID;

import com.Microservices.TopologyService.schemas.Line3D;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Line3DRepository extends CrudRepository<Line3D, UUID> {

}

