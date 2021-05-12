package com.Microservices.PostgresImportService.repositories;

import com.Microservices.PostgresImportService.schemas.Point;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PointRepository extends CrudRepository<Point, Integer> {

}
