package com.Microservices.PostgresImportService.repositories;

import com.Microservices.PostgresImportService.schemas.Line2D;

import org.springframework.stereotype.Repository;

@Repository
public interface Line2DRepository extends PostgresTableRepository<Line2D> {

}

