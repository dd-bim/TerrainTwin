package com.Microservices.PostgresImportService.repositories;

import java.util.UUID;

import com.Microservices.PostgresImportService.schemas.TIN;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TINRepository extends CrudRepository<TIN, UUID> {

}
