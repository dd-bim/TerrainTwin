package com.Microservices.DashboardService.repositories;

import com.Microservices.DashboardService.schemas.Line2D;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface Line2DRepository extends PostgresTableRepository<Line2D> {

    @Query(value = "select origin_id, count(*) from terraintwinv2.line_2d group by origin_id, geometry having count(*) > 1", nativeQuery = true)
    Integer[][] getLine2DDuplicates();
}

