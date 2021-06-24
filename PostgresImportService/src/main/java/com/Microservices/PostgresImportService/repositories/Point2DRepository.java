package com.Microservices.PostgresImportService.repositories;

import com.Microservices.PostgresImportService.schemas.Point2D;

import org.springframework.stereotype.Repository;

@Repository
public interface Point2DRepository extends PostgresTableRepository<Point2D> {

}
