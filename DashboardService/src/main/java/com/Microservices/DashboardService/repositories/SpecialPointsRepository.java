package com.Microservices.DashboardService.repositories;

import java.util.UUID;

import com.Microservices.DashboardService.schemas.SpecialPoints;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SpecialPointsRepository extends CrudRepository<SpecialPoints, UUID> {

    // @Query(value = "select point_id, count(*) from terraintwinv2.dtm_specialpoints group by point_id, tin_id, geometry having count(*) > 1", nativeQuery = true)
    // Integer[][] getSPntDuplicates();
}
