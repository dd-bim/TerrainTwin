package com.Microservices.PostgresImportService.repositories;

import com.Microservices.PostgresImportService.schemas.Line;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LineRepository extends CrudRepository<Line, Integer> {

}

