package com.Microservices.PostgresImportService.repositories;

import com.Microservices.PostgresImportService.schemas.Surfaces;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SurfaceRepository extends CrudRepository<Surfaces, Integer> {

}
