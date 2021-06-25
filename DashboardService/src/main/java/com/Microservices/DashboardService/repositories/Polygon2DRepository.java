package com.Microservices.DashboardService.repositories;

import com.Microservices.DashboardService.schemas.Polygon2D;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface Polygon2DRepository extends PostgresTableRepository<Polygon2D> {

    @Query(value = "select origin_id, count(*) from terraintwinv2.polygon_2d group by origin_id, geometry having count(*) > 1", nativeQuery = true)
    Integer[][] getPoly2DDuplicates();
}
