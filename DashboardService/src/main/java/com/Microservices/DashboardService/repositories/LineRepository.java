package com.Microservices.DashboardService.repositories;

import com.Microservices.DashboardService.schemas.Line;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LineRepository extends CrudRepository<Line, Integer> {

    @Query(value = "select line_id, count(*) from terraintwinv2.lines group by line_id, geometry having count(*) > 1", nativeQuery = true)
    Integer[][] getLineDuplicates();
}

