package com.Microservices.DashboardService.repositories;

import com.Microservices.DashboardService.schemas.TIN;

import org.springframework.stereotype.Repository;

@Repository
public interface TINRepository extends PostgresTableRepository<TIN> {

    // @Query(value = "select count(*) from terraintwinv2.dtm_tin group by geometry having count(*) > 1", nativeQuery = true)
    // Integer[] getTINDuplicates();
}
