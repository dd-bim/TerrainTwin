package com.Microservices.TopologyService.repositories;

import java.util.UUID;

import com.Microservices.TopologyService.schemas.Point3D;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Point3DRepository extends CrudRepository<Point3D, UUID> {

}
