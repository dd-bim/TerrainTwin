package com.Microservices.PostgresImportService.repositories;

import com.Microservices.PostgresImportService.schemas.Solid;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SolidRepository extends CrudRepository<Solid, Integer> {

}