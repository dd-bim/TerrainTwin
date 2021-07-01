package com.Microservices.PostgresImportService.repositories;

import java.util.UUID;

import com.Microservices.PostgresImportService.schemas.TIN;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface TINRepository extends PostgresTableRepository<TIN> {

    @Query(value = "select ST_asEWKT(geometry) from terraintwinv2.dtm_tin where id = ?#{[0]}", nativeQuery = true)
    String getTINGeometry(UUID id);
}
