package com.Microservices.DashboardService.repositories;

import java.util.UUID;

import com.Microservices.DashboardService.schemas.TIN;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TINRepository extends CrudRepository<TIN, UUID> {

    // @Query(value = "select count(*) from terraintwinv2.dtm_tin group by geometry having count(*) > 1", nativeQuery = true)
    // Integer[] getTINDuplicates();
}
