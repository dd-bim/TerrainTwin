package com.Microservices.PostgresImportService.repositories;

import com.Microservices.PostgresImportService.schemas.Point3D;

import org.springframework.stereotype.Repository;

@Repository
public interface Point3DRepository extends PostgresTableRepository<Point3D> {

}
