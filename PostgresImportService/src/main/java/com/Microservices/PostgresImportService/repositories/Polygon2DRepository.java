package com.Microservices.PostgresImportService.repositories;

import com.Microservices.PostgresImportService.schemas.Polygon2D;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Polygon2DRepository extends CrudRepository<Polygon2D, Integer> {

}
