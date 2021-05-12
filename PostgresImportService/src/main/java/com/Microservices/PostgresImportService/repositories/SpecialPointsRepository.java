package com.Microservices.PostgresImportService.repositories;

import com.Microservices.PostgresImportService.schemas.SpecialPoints;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SpecialPointsRepository extends CrudRepository<SpecialPoints, Integer> {

}
