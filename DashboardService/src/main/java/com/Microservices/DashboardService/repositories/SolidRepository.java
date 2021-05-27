package com.Microservices.DashboardService.repositories;

import com.Microservices.DashboardService.schemas.Solid;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SolidRepository extends CrudRepository<Solid, Integer> {

    @Query(value = "select solid_id, count(*) from terraintwinv2.solid group by solid_id, geometry having count(*) > 1", nativeQuery = true)
    Integer[][] getSolidDuplicates();
}
