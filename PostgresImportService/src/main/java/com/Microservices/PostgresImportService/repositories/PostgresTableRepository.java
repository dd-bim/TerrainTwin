package com.Microservices.PostgresImportService.repositories;

import java.util.UUID;

import com.Microservices.PostgresImportService.schemas.PostgresTables;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface PostgresTableRepository<T extends PostgresTables> extends CrudRepository<T, UUID> {
    
}
