package com.Microservices.PostgresImportService.repositories;

import java.util.UUID;

import com.Microservices.PostgresImportService.schemas.TIN;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface TINRepository extends PostgresTableRepository<TIN> {

    @Query(value = "select id, ST_ASEWKT(geometry) as geometry from #{#entityName} where id = ?#{[0]}", nativeQuery = true)
    TIN getItemTIN(UUID featureId);
}
