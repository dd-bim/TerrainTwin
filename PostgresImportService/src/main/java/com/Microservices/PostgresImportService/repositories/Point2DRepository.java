package com.Microservices.PostgresImportService.repositories;

import com.Microservices.PostgresImportService.schemas.Point2D;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Point2DRepository extends CrudRepository<Point2D, Integer> {

}
