package com.Microservices.PostgresImportService.repositories;

import com.Microservices.PostgresImportService.schemas.Solid;

import org.springframework.stereotype.Repository;

@Repository
public interface SolidRepository extends PostgresTableRepository<Solid> {

}
