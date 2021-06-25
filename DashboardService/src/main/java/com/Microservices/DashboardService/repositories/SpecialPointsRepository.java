package com.Microservices.DashboardService.repositories;

import com.Microservices.DashboardService.schemas.SpecialPoints;

import org.springframework.stereotype.Repository;

@Repository
public interface SpecialPointsRepository extends PostgresTableRepository<SpecialPoints> {

    // @Query(value = "select origin_id, count(*) from terraintwinv2.dtm_specialpoints group by origin_id, tin_id, geometry having count(*) > 1", nativeQuery = true)
    // Integer[][] getSPntDuplicates();
}
