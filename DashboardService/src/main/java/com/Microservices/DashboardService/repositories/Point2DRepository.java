package com.Microservices.DashboardService.repositories;

import com.Microservices.DashboardService.schemas.Point2D;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface Point2DRepository extends PostgresTableRepository<Point2D> {

    @Query(value = "select origin_id, count(*) from terraintwinv2.point_2d group by origin_id, geometry having count(*) > 1", nativeQuery = true)
    Integer[][] getPnt2DDuplicates();
}
