package com.Microservices.PostgresImportService.repositories;

import com.Microservices.PostgresImportService.schemas.Line3D;

import org.springframework.stereotype.Repository;

@Repository
public interface Line3DRepository extends PostgresTableRepository<Line3D> {

}

