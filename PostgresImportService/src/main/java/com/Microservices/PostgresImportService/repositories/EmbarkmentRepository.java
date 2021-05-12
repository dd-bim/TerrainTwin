package com.Microservices.PostgresImportService.repositories;

import com.Microservices.PostgresImportService.schemas.Embarkment;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmbarkmentRepository extends CrudRepository<Embarkment, Integer> {

}
