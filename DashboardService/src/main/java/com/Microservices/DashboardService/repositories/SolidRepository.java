package com.Microservices.DashboardService.repositories;

import com.Microservices.DashboardService.schemas.Solid;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface SolidRepository extends PostgresTableRepository<Solid> {

    @Query(value = "select origin_id, count(*) from terraintwinv2.solid group by origin_id, geometry having count(*) > 1", nativeQuery = true)
    Integer[][] getSolidDuplicates();
}
