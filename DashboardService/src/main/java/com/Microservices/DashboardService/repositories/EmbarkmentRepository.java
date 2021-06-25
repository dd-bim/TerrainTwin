package com.Microservices.DashboardService.repositories;

import com.Microservices.DashboardService.schemas.Embarkment;

import org.springframework.stereotype.Repository;

@Repository
public interface EmbarkmentRepository extends PostgresTableRepository<Embarkment> {

    // @Query(value = "select id, count(*) from terraintwinv2.dtm_embarkment group by id, tin_id, geometry having count(*) > 1", nativeQuery = true)
    // Integer[][] getEmbDuplicates();
}
