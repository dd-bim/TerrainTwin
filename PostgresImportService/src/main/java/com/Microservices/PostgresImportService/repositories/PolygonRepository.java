package com.Microservices.PostgresImportService.repositories;

import com.Microservices.PostgresImportService.schemas.Polygon;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PolygonRepository extends CrudRepository<Polygon, Integer> {

}
