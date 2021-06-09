package com.Microservices.PostgresImportService.repositories;

import com.Microservices.PostgresImportService.schemas.Polygon3D;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Polygon3DRepository extends CrudRepository<Polygon3D, Integer> {

}
