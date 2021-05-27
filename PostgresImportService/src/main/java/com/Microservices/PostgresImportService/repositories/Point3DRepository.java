package com.Microservices.PostgresImportService.repositories;

import com.Microservices.PostgresImportService.schemas.Point3D;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Point3DRepository extends CrudRepository<Point3D, Integer> {

}
