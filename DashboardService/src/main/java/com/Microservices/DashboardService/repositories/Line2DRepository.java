package com.Microservices.DashboardService.repositories;

import com.Microservices.DashboardService.schemas.Line2D;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Line2DRepository extends CrudRepository<Line2D, Integer> {

    @Query(value = "select line_id, count(*) from terraintwinv2.line_2d group by line_id, geometry having count(*) > 1", nativeQuery = true)
    Integer[][] getLine2DDuplicates();
}

